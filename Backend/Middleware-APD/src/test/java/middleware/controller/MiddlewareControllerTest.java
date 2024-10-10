package middleware.controller;

import middlewareapd.MiddlewareApplication;
import middlewareapd.dto.JWTRequest;
import middlewareapd.service.MiddlewareService;
import middlewareapd.util.UserNotFoundException;
import middlewareapd.util.UnauthorizedException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = MiddlewareApplication.class)
@AutoConfigureMockMvc
public class MiddlewareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MiddlewareService middlewareService;

    private JWTRequest validJwtRequest;
    private JWTRequest expiredJwtRequest;
    private JWTRequest invalidSignatureJwtRequest;

    @BeforeAll
    public static void setUpEnvironment() {
        Dotenv dotenv = Dotenv.configure()
            .load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
    }

    @BeforeEach
    public void setUp() {
        // Mock JWT request objects for testing different scenarios
        validJwtRequest = new JWTRequest();
        validJwtRequest.setJwt("valid-jwt-token");

        expiredJwtRequest = new JWTRequest();
        expiredJwtRequest.setJwt("expired-jwt-token");

        invalidSignatureJwtRequest = new JWTRequest();
        invalidSignatureJwtRequest.setJwt("invalid-signature-jwt-token");
    }

    @Test
    public void testCheckJwt_ValidToken() throws Exception {
        // Mock service to return valid result
        Mockito.when(middlewareService.checkJwt(any(JWTRequest.class)))
        .thenReturn(CompletableFuture.completedFuture(Map.of("uuid", "12345", "isAdmin", "true")));

        // Perform POST request and expect success response
        mockMvc.perform(post("/api/middleware/checkjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jwt\": \"valid-jwt-token\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("JWT validation successful.")))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.content.uuid", is("12345")))
                .andExpect(jsonPath("$.content.isAdmin", is("true")));
    }

    @Test
    public void testCheckJwt_UserNotFound() throws Exception {
        // Mock service to throw UserNotFoundException
        Mockito.when(middlewareService.checkJwt(any(JWTRequest.class)))
               .thenThrow(new UserNotFoundException("User not found"));

        // Perform POST request and expect 404 response
        mockMvc.perform(post("/api/middleware/checkjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jwt\": \"valid-jwt-token\" }"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found")))
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    public void testCheckJwt_Unauthorized() throws Exception {
        // Mock service to throw UnauthorizedException
        Mockito.when(middlewareService.checkJwt(any(JWTRequest.class)))
               .thenThrow(new UnauthorizedException("Unauthorized access"));

        // Perform POST request and expect 401 response
        mockMvc.perform(post("/api/middleware/checkjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jwt\": \"valid-jwt-token\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized access")))
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    public void testCheckJwt_InternalServerError() throws Exception {
        // Mock service to throw a generic Exception
        Mockito.when(middlewareService.checkJwt(any(JWTRequest.class)))
               .thenThrow(new RuntimeException("Unexpected error"));

        // Perform POST request and expect 500 response
        mockMvc.perform(post("/api/middleware/checkjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jwt\": \"valid-jwt-token\" }"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("Unexpected error")))
                .andExpect(jsonPath("$.success", is(false)));
    }
}

