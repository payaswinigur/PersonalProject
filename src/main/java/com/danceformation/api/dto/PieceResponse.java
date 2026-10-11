package com.danceformation.api.dto;

import java.util.List;

/**
 * The full state of one routine: its stage size and every formation slide.
 *
 * <p>
 * Every mutating endpoint returns this same shape, so a client can replace its
 * whole local copy with the response rather than reconciling a patch.
 * </p>
 *
 * <p>
 * Note what is deliberately absent: the component's internal "current slide"
 * cursor. Which slide you are editing is a UI concern, and the API addresses
 * slides by index instead, so the server keeps no per-client cursor.
 * </p>
 *
 * @param id
 *            identifier assigned at creation
 * @param width
 *            number of columns on the stage
 * @param height
 *            number of rows on the stage
 * @param formations
 *            the slides, in order
 */
public record PieceResponse(String id, int width, int height,
        List<Formation> formations) {

    /**
     * One formation slide.
     *
     * @param index
     *            position of this slide in the routine, 0-based
     * @param dancers
     *            the dancers on stage in this slide
     */
    public record Formation(int index, List<Dancer> dancers) {
    }

    /**
     * One dancer at one spot.
     *
     * @param name
     *            dancer name
     * @param x
     *            column index
     * @param y
     *            row index
     */
    public record Dancer(String name, int x, int y) {
    }
}
