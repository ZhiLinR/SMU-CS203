package middleware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;

import middleware.dto.JWTRequest;
import middleware.model.JWToken;
import middleware.repository.*;
import middleware.util.*;

import java.util.Map;

@Service
public class MiddlewareService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTokenRepository jwTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public Map<String, String> checkJwt(JWTRequest jwtRequest) {
        String jwt = jwtRequest.getJwt();
        Claims claims;

        // Decrypt JWT and extract claims
        try {
            claims = jwtUtil.decryptToken(jwt);
        } catch (RuntimeException e) {
            throw new UnauthorizedException("Invalid JWT or session expired.", e);
        }

        String extractedUuid = claims.get("uuid", String.class);
        Boolean isAdmin = claims.get("isAdmin", Boolean.class);

        // Validate JWT session data
        JWToken dbJwt = jwTokenRepository.checkValidity(jwt);
        ValidationUtil.validateJwtSession(dbJwt); // Moved validation logic

        String dbUuid = dbJwt.getUuid();
        ValidationUtil.validateUuid(dbUuid, extractedUuid); // Moved validation logic

        // Retrieve and check role for the user
        Byte dbIsAdmin = userRepository.getRoleByUUID(dbUuid);
        ValidationUtil.validateUserRole(dbIsAdmin, isAdmin); // Moved validation logic

        // Prepare and return the result as a map
        return Map.of("uuid", dbUuid, "isAdmin", isAdmin.toString());
    }
}