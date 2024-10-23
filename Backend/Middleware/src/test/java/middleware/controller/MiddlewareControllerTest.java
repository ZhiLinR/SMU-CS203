package middleware.controller;

import middleware.MiddlewareApplication;
import middleware.dto.JWTRequest;
import middleware.exception.UserNotFoundException;
import middleware.exception.UnauthorizedException;
import middleware.service.MiddlewareService;

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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the {@link MiddlewareController}.
 * <p>
 * This class tests various scenarios for the JWT validation endpoint, including valid, 
 * expired, and unauthorized tokens. It uses {@link MockMvc} to perform HTTP requests and 
 * verify responses from the controller layer. The service layer is mocked using {@link MockBean}.
 * </p>
 */
@SpringBootTest(classes = MiddlewareApplication.class)
@AutoConfigureMockMvc
public class MiddlewareControllerTest {

    /** {@link MockMvc} instance used to simulate HTTP requests and validate responses. */
    @Autowired
    private MockMvc mockMvc;

    /** Mocked instance of {@link MiddlewareService} to isolate service logic. */
    @MockBean
    private MiddlewareService middlewareService;

    /** JWT request with a valid token for testing successful validation. */
    private JWTRequest validJwtRequest;

    /** JWT request with an expired token for testing expiration scenarios. */
    private JWTRequest expiredJwtRequest;

    /** JWT request with an invalid signature for testing signature validation failure. */
    private JWTRequest invalidSignatureJwtRequest;

    /**
     * Sets up environment properties before any tests are executed.
     * <p>
     * Uses the <a href="https://github.com/cdimascio/dotenv-java">Dotenv</a> library 
     * to load environment variables and sets system properties required for the middleware.
     * </p>
     */
    @BeforeAll
    public static void setUpEnvironment() {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
    }

    /**
     * Initializes mock JWT request objects before each test case.
     * <p>
     * This ensures that each test starts with a fresh set of request objects.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        validJwtRequest = new JWTRequest();
        validJwtRequest.setJwt("valid-jwt-token");

        expiredJwtRequest = new JWTRequest();
        expiredJwtRequest.setJwt("expired-jwt-token");

        invalidSignatureJwtRequest = new JWTRequest();
        invalidSignatureJwtRequest.setJwt("invalid-signature-jwt-token");
    }

    /**
     * Tests the {@link MiddlewareController#checkJwt(JWTRequest)} endpoint with a valid JWT token.
     * <p>
     * Verifies that the response contains valid user data and returns HTTP 200 status.
     * </p>
     */
    @Test
    public void testCheckJwt_ValidToken() throws Exception {
        Mockito.when(middlewareService.checkJwt(any(JWTRequest.class)))
               .thenReturn(Map.of("uuid", "12345", "isAdmin", "true"));

        mockMvc.perform(post("/api/middleware/checkjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jwt\": \"valid-jwt-token\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("JWT validation successful.")))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.content.uuid", is("12345")))
                .andExpect(jsonPath("$.content.isAdmin", is("true")));
    }

    /**
     * Tests the {@link MiddlewareController#checkJwt(JWTRequest)} endpoint with a user not found scenario.
     * <p>
     * Verifies that the response returns HTTP 404 status with an appropriate error message.
     * </p>
     */
    @Test
    public void testCheckJwt_UserNotFound() throws Exception {
        Mockito.when(middlewareService.checkJwt(any(JWTRequest.class)))
               .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(post("/api/middleware/checkjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jwt\": \"valid-jwt-token\" }"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found")))
                .andExpect(jsonPath("$.success", is(false)));
    }

    /**
     * Tests the {@link MiddlewareController#checkJwt(JWTRequest)} endpoint with an unauthorized access scenario.
     * <p>
     * Verifies that the response returns HTTP 401 status with an appropriate error message.
     * </p>
     */
    @Test
    public void testCheckJwt_Unauthorized() throws Exception {
        Mockito.when(middlewareService.checkJwt(any(JWTRequest.class)))
               .thenThrow(new UnauthorizedException("Unauthorized access"));

        mockMvc.perform(post("/api/middleware/checkjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jwt\": \"valid-jwt-token\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized access")))
                .andExpect(jsonPath("$.success", is(false)));
    }

    /**
     * Tests the {@link MiddlewareController#checkJwt(JWTRequest)} endpoint with an internal server error scenario.
     * <p>
     * Verifies that the response returns HTTP 500 status with an appropriate error message.
     * </p>
     */
    @Test
    public void testCheckJwt_InternalServerError() throws Exception {
        Mockito.when(middlewareService.checkJwt(any(JWTRequest.class)))
               .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/middleware/checkjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"jwt\": \"valid-jwt-token\" }"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("Unexpected error")))
                .andExpect(jsonPath("$.success", is(false)));
    }
}
