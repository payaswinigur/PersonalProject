package com.danceformation.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.danceformation.api.dto.CreatePieceRequest;
import com.danceformation.api.dto.PieceResponse;
import com.danceformation.api.dto.PlaceDancerRequest;

/**
 * HTTP surface for routines.
 *
 * <p>
 * This class only maps requests onto PieceService and chooses status codes; all
 * rules live in the service. Every mutating endpoint returns the routine's full
 * new state, so a client can replace its local copy outright.
 * </p>
 *
 * <pre>
 * POST   /api/pieces                                 create a routine
 * GET    /api/pieces                                 list routine ids
 * GET    /api/pieces/{id}                            read a routine
 * DELETE /api/pieces/{id}                            delete a routine
 * POST   /api/pieces/{id}/formations                 append a copied slide
 * DELETE /api/pieces/{id}/formations/{i}             remove a slide
 * POST   /api/pieces/{id}/formations/{i}/dancers     place a dancer
 * DELETE /api/pieces/{id}/formations/{i}/dancers     clear a spot
 * </pre>
 */
@RestController
@RequestMapping("/api/pieces")
public class PieceController {

    /** The rules behind these endpoints. */
    private final PieceService service;

    /**
     * Creates the controller.
     *
     * @param service
     *            the service to delegate to
     */
    public PieceController(PieceService service) {
        this.service = service;
    }

    /**
     * Creates a routine with one empty formation.
     *
     * @param request
     *            the stage size
     * @return 201 with the new routine, and its URL in the Location header
     */
    @PostMapping
    public ResponseEntity<PieceResponse> create(
            @RequestBody CreatePieceRequest request) {
        PieceResponse piece = this.service.create(request.width(),
                request.height());
        return ResponseEntity.created(URI.create("/api/pieces" + "/"
                + piece.id())).body(piece);
    }

    /**
     * Lists the ids of all stored routines.
     *
     * @return 200 with the ids
     */
    @GetMapping
    public List<String> list() {
        return this.service.listIds();
    }

    /**
     * Reads one routine.
     *
     * @param id
     *            routine id
     * @return 200 with the routine
     */
    @GetMapping("/{id}")
    public PieceResponse read(@PathVariable String id) {
        return this.service.read(id);
    }

    /**
     * Deletes one routine.
     *
     * @param id
     *            routine id
     * @return 204 with no body
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Appends a formation copying the last one.
     *
     * @param id
     *            routine id
     * @return 201 with the updated routine
     */
    @PostMapping("/{id}/formations")
    public ResponseEntity<PieceResponse> addFormation(
            @PathVariable String id) {
        return ResponseEntity.status(201).body(this.service.addFormation(id));
    }

    /**
     * Removes one formation.
     *
     * @param id
     *            routine id
     * @param index
     *            slide to remove
     * @return 200 with the updated routine
     */
    @DeleteMapping("/{id}/formations/{index}")
    public PieceResponse removeFormation(@PathVariable String id,
            @PathVariable int index) {
        return this.service.removeFormation(id, index);
    }

    /**
     * Places a dancer on a spot.
     *
     * @param id
     *            routine id
     * @param index
     *            slide to edit
     * @param request
     *            dancer name and spot
     * @return 200 with the updated routine
     */
    @PostMapping("/{id}/formations/{index}/dancers")
    public PieceResponse placeDancer(@PathVariable String id,
            @PathVariable int index,
            @RequestBody PlaceDancerRequest request) {
        return this.service.placeDancer(id, index, request.name(),
                request.x(), request.y());
    }

    /**
     * Clears a spot.
     *
     * @param id
     *            routine id
     * @param index
     *            slide to edit
     * @param x
     *            column index
     * @param y
     *            row index
     * @return 200 with the updated routine
     */
    @DeleteMapping("/{id}/formations/{index}/dancers")
    public PieceResponse removeDancer(@PathVariable String id,
            @PathVariable int index, @RequestParam int x,
            @RequestParam int y) {
        return this.service.removeDancer(id, index, x, y);
    }
}
