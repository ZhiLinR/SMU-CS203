package middleware.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import middleware.dto.*;
import middleware.exception.UserNotFoundException;
import middleware.exception.UnauthorizedException;
import middleware.service.MiddlewareService;
import middleware.util.*;

import org.springframework.http.HttpStatus;

/**
 * MiddlewareController is a REST controller that handles HTTP requests related
 * to middleware services.
 * It provides endpoints for validating JWT tokens.
 *
 * <p>
 * This controller is mapped to the base URL "/api".
 * </p>
 */
@RestController
@RequestMapping("/api")
public class MiddlewareController {

    @Autowired
    private MiddlewareService middlewareService;

    /**
     * Validates the provided JWT token by calling the MiddlewareService.
     *
     * <p>
     * The endpoint is mapped to "/jwt" and accepts POST requests.
     * It expects a JSON request body containing the JWTRequest object.
     * </p>
     *
     * @param jwtRequest the request body containing the JWT token to be validated.
     * @return a ResponseEntity containing a success message and the validation
     *         result if the JWT is valid,
     *         or an error message with the appropriate HTTP status if validation
     *         fails.
     */
    @PostMapping("/jwt")
    public ResponseEntity<Map<String, Object>> checkJwt(@RequestBody JWTRequest jwtRequest) {
        // Call the async service method
        CompletableFuture<Map<String, String>> resultFuture = middlewareService.checkJwt(jwtRequest);

        return resultFuture
                .thenApply(result -> ResponseManager.success("JWT validation successful.", result)) // On success
                .exceptionally(ex -> {
                    // Handle exceptions
                    Throwable cause = ex.getCause(); // Get the cause of the exception
                    if (cause instanceof UserNotFoundException) {
                        return ResponseManager.error(HttpStatus.NOT_FOUND, cause.getMessage());
                    } else if (cause instanceof UnauthorizedException) {
                        return ResponseManager.error(HttpStatus.UNAUTHORIZED, cause.getMessage());
                    } else {
                        // For unexpected errors, provide a generic message
                        return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                cause.getMessage());
                    }
                })
                .join(); // Waits for the async process to complete and return the response
    }
}
