package com.danceformation.api.dto;

/**
 * Body of POST /api/pieces: the stage size for a new routine.
 *
 * @param width
 *            number of columns on the stage
 * @param height
 *            number of rows on the stage
 */
public record CreatePieceRequest(Integer width, Integer height) {
}
