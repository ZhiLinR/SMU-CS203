package middleware.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
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
 * to middleware services. It provides endpoints for validating JWT tokens.
 *
 * <p>
 * This controller is mapped to the base URL "/api".
 * </p>
 */
@CrossOrigin(origins = "${ORIGIN}")
@RestController
@RequestMapping("/api")
public class MiddlewareController {

    /**
     * Origin URL, set via `ORIGIN` property.
     */
    @Value("${ORIGIN}")
    private String origin;

    @Autowired
    private MiddlewareService middlewareService;

    /**
     * Performs a health check for the application.
     *
     * This method is exposed as a GET endpoint to verify that the application is
     * running and responsive. It returns a success message if the health check
     * passes or an error message if an unexpected issue occurs during the check.
     *
     * @return a {@link ResponseEntity} containing a map with success or error
     *         message
     *         indicating the health status of the application
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            return ResponseManager.success("Health Check Success");
        } catch (Exception e) {
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    /**
     * Validates the provided JWT token by calling the MiddlewareService.
     *
     * <p>
     * The endpoint is mapped to "/middleware/checkjwt" and accepts POST requests.
     * It expects a JSON request body containing the JWTRequest object.
     * </p>
     *
     * @param jwtRequest the request body containing the JWT token to be validated.
     * @return a ResponseEntity containing a success message and the validation
     *         result if the JWT is valid,
     *         or an error message with the appropriate HTTP status if validation
     *         fails.
     * @throws UserNotFoundException if the user associated with the JWT is not
     *                               found.
     * @throws UnauthorizedException if the JWT token is invalid or unauthorized.
     * @throws Exception             for any other unexpected errors.
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
                    } else if (cause instanceof IllegalArgumentException) {
                        return ResponseManager.error(HttpStatus.BAD_REQUEST, cause.getMessage());
                    } else {
                        // For unexpected errors, provide a generic message
                        return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                cause.getMessage());
                    }
                })
                .join(); // Waits for the async process to complete and return the response
    }
}
