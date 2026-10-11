package com.danceformation.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.danceformation.api.dto.PieceResponse;
import com.danceformation.components.DanceFormation;

/**
 * The rules of the API, kept out of the controller.
 *
 * <p>
 * Two jobs that the component deliberately does not do:
 * </p>
 * <ul>
 * <li><b>Reporting refusals.</b> addDancer silently ignores a placement onto
 * an occupied spot and removeFormation silently ignores the last slide. Both
 * are fine inside Java but indistinguishable from success over HTTP, so this
 * class checks the condition first and throws instead.</li>
 * <li><b>Serializing access.</b> DanceFormation1 is not thread safe, and a web
 * server handles requests concurrently. Every method below synchronizes on the
 * routine it touches, so two requests editing one routine cannot interleave.
 * Different routines still proceed in parallel.</li>
 * </ul>
 */
@Service
public class PieceService {

    /** Largest stage side accepted, to bound response sizes. */
    private static final int MAX_SIDE = 100;

    /** Where routines are stored. */
    private final InMemoryPieceRepository repository;

    /**
     * Creates the service.
     *
     * @param repository
     *            storage for routines
     */
    public PieceService(InMemoryPieceRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a routine with one empty formation.
     *
     * @param width
     *            number of columns
     * @param height
     *            number of rows
     * @return the new routine
     */
    public PieceResponse create(Integer width, Integer height) {
        int w = required(width, "width");
        int h = required(height, "height");

        // The component's representation invariant requires positive
        // dimensions, so reject rather than build a broken routine
        if (w < 1 || h < 1 || w > MAX_SIDE || h > MAX_SIDE) {
            throw ApiException.badRequest("invalid_stage_size",
                    "width and height must be between 1 and " + MAX_SIDE);
        }

        String id = this.repository.create(w, h);
        return this.read(id);
    }

    /**
     * Returns a routine's full state.
     *
     * @param id
     *            routine id
     * @return the routine
     */
    public PieceResponse read(String id) {
        DanceFormation piece = this.require(id);
        synchronized (piece) {
            return snapshot(id, piece);
        }
    }

    /**
     * Lists the ids of all stored routines.
     *
     * @return the ids
     */
    public List<String> listIds() {
        return this.repository.ids();
    }

    /**
     * Deletes a routine.
     *
     * @param id
     *            routine id
     */
    public void delete(String id) {
        if (!this.repository.delete(id)) {
            throw ApiException.notFound("no routine with id " + id);
        }
    }

    /**
     * Appends a formation that copies the last one, matching the component's
     * copy-forward behavior so each slide starts from the previous pose.
     *
     * @param id
     *            routine id
     * @return the updated routine
     */
    public PieceResponse addFormation(String id) {
        DanceFormation piece = this.require(id);
        synchronized (piece) {

            // addFormation copies whichever slide is current, so point it at
            // the last slide to make the result independent of past requests
            piece.setCurrentFormation(piece.numberOfFormations() - 1);
            piece.addFormation();
            return snapshot(id, piece);
        }
    }

    /**
     * Removes one formation.
     *
     * @param id
     *            routine id
     * @param index
     *            slide to remove
     * @return the updated routine
     */
    public PieceResponse removeFormation(String id, int index) {
        DanceFormation piece = this.require(id);
        synchronized (piece) {
            this.requireFormation(piece, index);

            // removeFormation ignores the request when one slide is left,
            // which over HTTP would look like a successful deletion
            if (piece.numberOfFormations() == 1) {
                throw ApiException.conflict("last_formation",
                        "a routine must keep at least one formation");
            }

            piece.setCurrentFormation(index);
            piece.removeFormation();
            return snapshot(id, piece);
        }
    }

    /**
     * Places a dancer on a spot in one formation. A dancer already on stage is
     * moved rather than duplicated.
     *
     * @param id
     *            routine id
     * @param index
     *            slide to edit
     * @param name
     *            dancer name
     * @param x
     *            column index
     * @param y
     *            row index
     * @return the updated routine
     */
    public PieceResponse placeDancer(String id, int index, String name,
            Integer x, Integer y) {
        DanceFormation piece = this.require(id);
        synchronized (piece) {
            this.requireFormation(piece, index);

            if (name == null || name.isBlank()) {
                throw ApiException.badRequest("invalid_name",
                        "name must not be empty");
            }
            int px = required(x, "x");
            int py = required(y, "y");
            this.requireOnStage(piece, px, py);

            piece.setCurrentFormation(index);

            // addDancer refuses an occupied spot without saying so, so check
            // first and report who is in the way
            String occupant = piece.dancerAt(px, py);
            if (occupant != null && !occupant.equals(name)) {
                throw ApiException.occupied(px, py, occupant);
            }

            piece.addDancer(name, px, py);
            return snapshot(id, piece);
        }
    }

    /**
     * Clears a spot in one formation. Succeeds whether or not anyone was
     * there, so that repeating the request is harmless.
     *
     * @param id
     *            routine id
     * @param index
     *            slide to edit
     * @param x
     *            column index
     * @param y
     *            row index
     * @return the updated routine
     */
    public PieceResponse removeDancer(String id, int index, int x, int y) {
        DanceFormation piece = this.require(id);
        synchronized (piece) {
            this.requireFormation(piece, index);
            this.requireOnStage(piece, x, y);

            piece.setCurrentFormation(index);
            piece.removeDancer(x, y);
            return snapshot(id, piece);
        }
    }

    /**
     * Reads every slide into a response, using only the public interface.
     *
     * @param id
     *            routine id
     * @param piece
     *            the routine
     * @return a snapshot of the routine
     */
    private static PieceResponse snapshot(String id, DanceFormation piece) {
        int width = piece.formationWidth();
        int height = piece.formationHeight();
        int slides = piece.numberOfFormations();

        List<PieceResponse.Formation> formations = new ArrayList<>();
        for (int i = 0; i < slides; i++) {

            // Reading a slide means making it current, since the interface
            // exposes dancerAt only for the current formation
            piece.setCurrentFormation(i);

            List<PieceResponse.Dancer> dancers = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    String name = piece.dancerAt(x, y);
                    if (name != null) {
                        dancers.add(new PieceResponse.Dancer(name, x, y));
                    }
                }
            }
            formations.add(new PieceResponse.Formation(i, dancers));
        }
        return new PieceResponse(id, width, height, formations);
    }

    /**
     * Finds a routine or fails with 404.
     *
     * @param id
     *            routine id
     * @return the routine
     */
    private DanceFormation require(String id) {
        DanceFormation piece = this.repository.find(id);
        if (piece == null) {
            throw ApiException.notFound("no routine with id " + id);
        }
        return piece;
    }

    /**
     * Checks that a slide index exists.
     *
     * @param piece
     *            the routine
     * @param index
     *            slide index
     */
    private void requireFormation(DanceFormation piece, int index) {
        if (index < 0 || index >= piece.numberOfFormations()) {
            throw ApiException.notFound("no formation at index " + index);
        }
    }

    /**
     * Checks that a position is on the stage.
     *
     * @param piece
     *            the routine
     * @param x
     *            column index
     * @param y
     *            row index
     */
    private void requireOnStage(DanceFormation piece, int x, int y) {
        if (x < 0 || x >= piece.formationWidth() || y < 0
                || y >= piece.formationHeight()) {
            throw ApiException.badRequest("off_stage",
                    "position (" + x + ", " + y + ") is outside the "
                            + piece.formationWidth() + "x"
                            + piece.formationHeight() + " stage");
        }
    }

    /**
     * Requires a body field to be present.
     *
     * @param value
     *            the field value
     * @param field
     *            field name, for the error message
     * @return the value
     */
    private static int required(Integer value, String field) {
        if (value == null) {
            throw ApiException.badRequest("missing_field",
                    field + " is required");
        }
        return value;
    }
}
