package middleware.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import middleware.dto.*;
import middleware.service.MiddlewareService;
import middleware.util.*;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api")
public class MiddlewareController {

    @Autowired
    private MiddlewareService middlewareService;

    @PostMapping("/checkjwt")
    public ResponseEntity<Map<String, Object>> checkJwt(@RequestBody JWTRequest jwtRequest) {
        try {
            // Call the service function to check the JWT
            Map<String, String> result = middlewareService.checkJwt(jwtRequest);
            // If successful, return a success response
            return ResponseManager.success("JWT validation successful.", result);
        } catch (UserNotFoundException e) {
            // Return 404 if the user is not found
            return ResponseManager.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            // Return 401 for unauthorized access
            return ResponseManager.error(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            // Return 500 for any other unexpected errors
            return ResponseManager.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }
}

