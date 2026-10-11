package com.danceformation.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.danceformation.components.DanceFormation;
import com.danceformation.components.DanceFormation1;

/**
 * Holds every routine in memory, keyed by a generated id.
 *
 * <p>
 * Everything here dies with the process. PostgreSQL replaces this in a later
 * milestone, which is why the rest of the API talks to this class rather than
 * to a map directly: swapping the storage should not touch the controller.
 * </p>
 */
@Repository
public class InMemoryPieceRepository {

    /** Routines by id. */
    private final Map<String, DanceFormation> pieces =
            new ConcurrentHashMap<>();

    /** Source of the next id. */
    private final AtomicLong nextId = new AtomicLong(1);

    /**
     * Stores a new routine with the given stage size.
     *
     * @param width
     *            number of columns
     * @param height
     *            number of rows
     * @return the id assigned to the new routine
     */
    public String create(int width, int height) {
        String id = Long.toString(this.nextId.getAndIncrement());
        this.pieces.put(id, new DanceFormation1(width, height));
        return id;
    }

    /**
     * Looks up a routine.
     *
     * @param id
     *            routine id
     * @return the routine, or null if no routine has that id
     */
    public DanceFormation find(String id) {
        return this.pieces.get(id);
    }

    /**
     * Removes a routine.
     *
     * @param id
     *            routine id
     * @return true if a routine was removed
     */
    public boolean delete(String id) {
        return this.pieces.remove(id) != null;
    }

    /**
     * Lists the ids of every stored routine.
     *
     * @return the ids, in no particular order
     */
    public List<String> ids() {
        return new ArrayList<>(this.pieces.keySet());
    }
}
