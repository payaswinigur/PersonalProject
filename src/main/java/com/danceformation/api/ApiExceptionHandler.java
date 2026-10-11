package com.danceformation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.danceformation.api.dto.ApiError;

/**
 * Turns ApiException into its HTTP status and JSON body.
 *
 * <p>
 * Without this, Spring would report every ApiException as a 500, and the
 * frontend could not tell an occupied spot from a server fault.
 * </p>
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Renders an ApiException as its intended response.
     *
     * @param failure
     *            the exception thrown by the service
     * @return the status and body the exception carries
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handle(ApiException failure) {
        return ResponseEntity.status(failure.status()).body(failure.body());
    }
}
