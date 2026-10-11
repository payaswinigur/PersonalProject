package com.danceformation.api.dto;

/**
 * Body of POST /api/pieces/{id}/formations/{index}/dancers: a dancer and the
 * spot to put them on.
 *
 * @param name
 *            dancer name
 * @param x
 *            column index
 * @param y
 *            row index
 */
public record PlaceDancerRequest(String name, Integer x, Integer y) {
}
