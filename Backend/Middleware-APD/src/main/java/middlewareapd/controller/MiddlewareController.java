package middlewareapd.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import middlewareapd.dto.*;
import middlewareapd.service.MiddlewareService;
import middlewareapd.util.*;

import org.springframework.http.HttpStatus;

/**
 * MiddlewareController is a REST controller that handles HTTP requests related to middleware services.
 * It provides endpoints for validating JWT tokens.
 *
 * <p>This controller is mapped to the base URL "/api".</p>
 */
@RestController
@RequestMapping("/api")
public class MiddlewareController {

    @Autowired
    private MiddlewareService middlewareService;

    /**
     * Validates the provided JWT token by calling the MiddlewareService.
     * 
     * <p>The endpoint is mapped to "/middleware/checkjwt" and accepts POST requests.
     * It expects a JSON request body containing the JWTRequest object.</p>
     * 
     * @param jwtRequest the request body containing the JWT token to be validated.
     * @return a ResponseEntity containing a success message and the validation result if the JWT is valid,
     *         or an error message with the appropriate HTTP status if validation fails.
     * @throws UserNotFoundException if the user associated with the JWT is not found.
     * @throws UnauthorizedException if the JWT token is invalid or unauthorized.
     * @throws Exception for any other unexpected errors.
     */
    @PostMapping("/middleware/checkjwt")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> checkJwt(@RequestBody JWTRequest jwtRequest) {

        return middlewareService.checkJwt(jwtRequest)
            // Set a timeout for the JWT check operation (e.g., 5 seconds)
            .orTimeout(5, TimeUnit.SECONDS)
            .thenApply(result -> {
                // If successful, return a success response
                return ResponseManager.success("JWT validation successful.", result);
            })
            .exceptionally(ex -> {
                // Handle exceptions based on their type
                if (ex.getCause() instanceof UserNotFoundException) {
                    return ResponseManager.error(HttpStatus.NOT_FOUND, ex.getCause().getMessage());
                } else if (ex.getCause() instanceof UnauthorizedException) {
                    return ResponseManager.error(HttpStatus.UNAUTHORIZED, ex.getCause().getMessage());
                } else if (ex.getCause() instanceof TimeoutException) {
                    return ResponseManager.error(HttpStatus.REQUEST_TIMEOUT, "Request timed out after 5 seconds.");
                } else {
                    return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
                }
            });
    }
}

