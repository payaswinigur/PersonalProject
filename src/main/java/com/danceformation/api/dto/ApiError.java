package com.danceformation.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Error body returned with any non-2xx response.
 *
 * <p>
 * <code>error</code> is a stable machine-readable code the frontend can switch
 * on; <code>message</code> is for humans. The remaining fields are populated
 * only where they apply, and omitted from the JSON otherwise.
 * </p>
 *
 * @param error
 *            stable error code, for example position_occupied
 * @param message
 *            human-readable explanation
 * @param x
 *            column index the request referred to, when relevant
 * @param y
 *            row index the request referred to, when relevant
 * @param occupant
 *            the dancer already holding the spot, for position_occupied
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(String error, String message, Integer x, Integer y,
        String occupant) {

    /**
     * Builds an error carrying only a code and a message.
     *
     * @param error
     *            stable error code
     * @param message
     *            human-readable explanation
     */
    public ApiError(String error, String message) {
        this(error, message, null, null, null);
    }
}
