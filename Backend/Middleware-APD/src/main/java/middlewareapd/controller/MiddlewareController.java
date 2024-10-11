package middlewareapd.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
     * @param jwtRequest the request body containing the JWT token to be validated.
     * @return a ResponseEntity containing a success message and the validation result if the JWT is valid,
     *         or an error message with the appropriate HTTP status if validation fails.
     */
    @PostMapping("/middleware/checkjwt")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> checkJwt(@RequestBody JWTRequest jwtRequest) {
        return middlewareService.checkJwt(jwtRequest)
            .thenApply(result -> ResponseManager.success("JWT validation successful.", result))
            .exceptionally(e -> {
                if (e.getCause() instanceof UserNotFoundException) {
                    return ResponseManager.error(HttpStatus.NOT_FOUND, e.getCause().getMessage());
                } else if (e.getCause() instanceof UnauthorizedException) {
                    return ResponseManager.error(HttpStatus.UNAUTHORIZED, e.getCause().getMessage());
                } else {
                    return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                }
            });
    }
}