// package middlewareapd;

// import middlewareapd.dto.JWTRequest;
// import middlewareapd.model.JWToken;
// import middlewareapd.repository.JWTokenRepository;
// import middlewareapd.service.MiddlewareService;
// import middlewareapd.util.JwtUtil;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.scheduling.annotation.AsyncResult;
// import org.springframework.scheduling.annotation.EnableAsync;
// import org.springframework.test.context.ActiveProfiles;

// import io.github.cdimascio.dotenv.Dotenv;
// import io.jsonwebtoken.Claims;
// import jakarta.persistence.EntityManager;

// import java.util.Map;
// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.ExecutionException;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.when;

// class MiddlewareServiceAsyncTest {

//     @InjectMocks
//     private MiddlewareService middlewareService;

//     @Mock
//     private JWTokenRepository jwTokenRepository;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testCheckJwtAsyncBehavior() throws Exception {
//         // Arrange
//         JWToken mockToken = new JWToken();
//         mockToken.setJwt("mockJwtToken");
//         mockToken.setUuid("mockUUID");
//         mockToken.setLogout(null);

//         when(jwTokenRepository.findByJwt("mockJwtToken")).thenReturn(mockToken);

//         // Act
//         CompletableFuture<Boolean> futureResult = middlewareService.checkJwtAsync("mockJwtToken");

//         // Wait for the result
//         Boolean result = futureResult.get(); // This will block until the future is complete

//         // Assert
//         assertTrue(result); // Or whatever your expected behavior is
//     }
// }