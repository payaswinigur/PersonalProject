package com.danceformation.api;

import org.springframework.http.HttpStatus;

import com.danceformation.api.dto.ApiError;

/**
 * A failure that maps directly onto an HTTP status and error body.
 *
 * <p>
 * The static factories keep status codes in one place rather than scattering
 * them through the service, and they document which situations the API treats
 * as the client's fault (400), a missing thing (404), or a clash with current
 * state (409).
 * </p>
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** The HTTP status to respond with. */
    private final HttpStatus status;

    /** The body to respond with. */
    private final ApiError body;

    /**
     * Creates an exception carrying a status and body.
     *
     * @param status
     *            HTTP status to respond with
     * @param body
     *            error body to respond with
     */
    public ApiException(HttpStatus status, ApiError body) {
        super(body.message());
        this.status = status;
        this.body = body;
    }

    /**
     * @return the HTTP status to respond with
     */
    public HttpStatus status() {
        return this.status;
    }

    /**
     * @return the error body to respond with
     */
    public ApiError body() {
        return this.body;
    }

    /**
     * The requested routine or slide does not exist.
     *
     * @param message
     *            human-readable explanation
     * @return a 404 exception
     */
    public static ApiException notFound(String message) {
        return new ApiException(HttpStatus.NOT_FOUND,
                new ApiError("not_found", message));
    }

    /**
     * The request itself is malformed or out of range.
     *
     * @param code
     *            stable error code
     * @param message
     *            human-readable explanation
     * @return a 400 exception
     */
    public static ApiException badRequest(String code, String message) {
        return new ApiException(HttpStatus.BAD_REQUEST,
                new ApiError(code, message));
    }

    /**
     * The request is valid but clashes with the current state.
     *
     * @param code
     *            stable error code
     * @param message
     *            human-readable explanation
     * @return a 409 exception
     */
    public static ApiException conflict(String code, String message) {
        return new ApiException(HttpStatus.CONFLICT,
                new ApiError(code, message));
    }

    /**
     * Another dancer already holds the requested spot.
     *
     * @param x
     *            column index
     * @param y
     *            row index
     * @param occupant
     *            the dancer already there
     * @return a 409 exception naming the occupant
     */
    public static ApiException occupied(int x, int y, String occupant) {
        return new ApiException(HttpStatus.CONFLICT,
                new ApiError("position_occupied",
                        occupant + " already holds (" + x + ", " + y + ")",
                        x, y, occupant));
    }
}
