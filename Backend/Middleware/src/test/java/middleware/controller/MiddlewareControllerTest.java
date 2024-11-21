package middleware.controller;

import middleware.MiddlewareApplication;
import middleware.exception.UserNotFoundException;
import middleware.exception.UnauthorizedException;
import middleware.service.MiddlewareService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the {@link MiddlewareController}.
 * <p>
 * This class tests various scenarios for the JWT validation endpoint, including
 * valid,
 * expired, and unauthorized tokens. It uses {@link MockMvc} to perform HTTP
 * requests and
 * verify responses from the controller layer. The service layer is mocked using
 * {@link MockBean}.
 * </p>
 */
@SpringBootTest(classes = MiddlewareApplication.class)
@AutoConfigureMockMvc
public class MiddlewareControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private MiddlewareService middlewareService;

        @BeforeAll
        public static void setUpEnvironment() {
                Dotenv dotenv = Dotenv.configure().load();
                System.setProperty("DB_URL", dotenv.get("DB_URL"));
                System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
                System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
                System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
                System.setProperty("ORIGIN", dotenv.get("ORIGIN"));
        }

        @Test
        public void testCheckJwt_ValidToken() throws Exception {
                Mockito.when(middlewareService.checkJwt("valid-jwt-token"))
                                .thenReturn(CompletableFuture
                                                .completedFuture(Map.of("uuid", "12345", "isAdmin", "true")));

                mockMvc.perform(get("/api/auth/jwt")
                                .header("jwt", "valid-jwt-token"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message", is("JWT validation successful.")))
                                .andExpect(jsonPath("$.success", is(true)))
                                .andExpect(jsonPath("$.content.uuid", is("12345")))
                                .andExpect(jsonPath("$.content.isAdmin", is("true")));
        }

        @Test
        public void testCheckJwt_UserNotFound() throws Exception {
                Mockito.when(middlewareService.checkJwt("valid-jwt-token"))
                                .thenReturn(CompletableFuture
                                                .failedFuture(new UserNotFoundException("User not found")));

                mockMvc.perform(get("/api/auth/jwt")
                                .header("jwt", "valid-jwt-token"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message", is("User not found")))
                                .andExpect(jsonPath("$.success", is(false)));
        }

        @Test
        public void testCheckJwt_Unauthorized() throws Exception {
                Mockito.when(middlewareService.checkJwt("valid-jwt-token"))
                                .thenReturn(CompletableFuture
                                                .failedFuture(new UnauthorizedException("Unauthorized access")));

                mockMvc.perform(get("/api/auth/jwt")
                                .header("jwt", "valid-jwt-token"))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.message", is("Unauthorized access")))
                                .andExpect(jsonPath("$.success", is(false)));
        }

        @Test
        public void testCheckJwt_InternalServerError() throws Exception {
                Mockito.when(middlewareService.checkJwt("valid-jwt-token"))
                                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Unexpected error")));

                mockMvc.perform(get("/api/auth/jwt")
                                .header("jwt", "valid-jwt-token"))
                                .andExpect(status().isInternalServerError())
                                .andExpect(jsonPath("$.message", is("Unexpected error")))
                                .andExpect(jsonPath("$.success", is(false)));
        }
}
