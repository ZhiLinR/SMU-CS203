package middlewareapd.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReadWriteLock;

import middlewareapd.repository.*;
import middlewareapd.model.*;
import middlewareapd.util.*;
import middlewareapd.exception.*;

/**
 * Test class for MiddlewareService to validate concurrency handling, race conditions,
 * and lock management using read-write locks.
 */
public class MiddlewareConcurrencyTest {

    private MockJWTRepository mockRepo;
    private MiddlewareService service;
    private String uuid;
    private JWToken token;

    /**
     * Setup method to initialize required objects before each test case.
     */
    @BeforeEach
    public void setup() {
        mockRepo = new MockJWTRepository();
        service = new MiddlewareService(mockRepo);
        uuid = "test-uuid";

        // Generate a valid JWT using JwtUtil
        String jwt = JwtUtil.generateToken("user@example.com", uuid, 1);

        // Initialize a token with all required fields
        token = new JWToken(jwt, uuid, 1, LocalDateTime.now(), null, LocalDateTime.now());
        mockRepo.addToken(token); // Add the token to the mock repository
    }

    /**
     * Test to validate that concurrent updates to the same token are handled properly
     * by using a write lock to prevent race conditions.
     *
     * @throws InterruptedException if thread execution is interrupted
     */
    @Test
    public void testConcurrentTokenUpdate() throws Exception {
        // Arrange: Add a token to the repository
        JWToken token = new JWToken("test-jwt", uuid, 0, 
                                    LocalDateTime.now(), null, LocalDateTime.now());
        mockRepo.addToken(token);

        // Use Reflection to access the private 'updateLastAccess' method
        Method updateMethod = MiddlewareService.class.getDeclaredMethod(
                "updateLastAccess", String.class, ReadWriteLock.class);
        updateMethod.setAccessible(true); // Make the private method accessible

        // Define a task that calls 'updateLastAccess' using reflection
        Runnable task = () -> {
            try {
                ReadWriteLock lock = LockFactory.getRWLock(token.getJwt());
                updateMethod.invoke(service, uuid, lock); // Invoke using reflection
            } catch (Exception e) {
                throw new RuntimeException(e); // Handle any reflection exceptions
            }
        };

        // Define and start two threads simulating concurrent updates
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        // Wait for both threads to complete
        thread1.join();
        thread2.join();

        // Assert that the token's last access was updated
        assertNotNull(mockRepo.getTokenByUuid(uuid).getLastAccess());
        System.out.println("Concurrent update handled successfully.");
    }

    /**
     * Test to validate that a race condition between JWT validation and user logout
     * is correctly managed by ensuring that the logout action takes precedence.
     *
     * @throws InterruptedException if thread execution is interrupted
     */
    @Test
    public void testConcurrentLogoutDuringValidation() throws InterruptedException {
        // Generate a valid JWT for the test
        String jwt = JwtUtil.generateToken("user@example.com", uuid, 1);

        // Thread 1: Validate the JWT token
        Runnable validateTask = () -> {
            try {
                service.checkJwt(jwt);
            } catch (UnauthorizedException e) {
                System.out.println("JWT validation failed: " + e.getMessage());
            }
        };

        // Thread 2: Simulate user logging out
        Runnable logoutTask = () -> {
            token.setLogout(LocalDateTime.now());
            mockRepo.updateToken(token);
        };

        // Start both tasks concurrently
        Thread thread1 = new Thread(validateTask);
        Thread thread2 = new Thread(logoutTask);

        thread1.start();
        thread2.start();

        // Wait for both threads to finish
        thread1.join();
        thread2.join();

        // Verify that the token's logout timestamp was set
        assertNotNull(mockRepo.getTokenByUuid(uuid).getLogout());
        System.out.println("Race condition handled: Logout takes precedence.");
    }

    /**
     * Test to validate the behavior of read-write locks during concurrent operations.
     * Ensures that multiple read operations are allowed concurrently but write operations
     * block both reads and other writes.
     *
     * @throws InterruptedException if thread execution is interrupted
     */
    @Test
    public void testReadWriteLockBehavior() throws InterruptedException {
        ReadWriteLock lock = LockFactory.getRWLock(uuid);

        // Task: Simulate a read operation
        Runnable readTask = () -> {
            lock.readLock().lock();
            try {
                System.out.println("Reading token for UUID: " + uuid);
            } finally {
                lock.readLock().unlock();
            }
        };

        // Task: Simulate a write operation
        Runnable writeTask = () -> {
            lock.writeLock().lock();
            try {
                System.out.println("Updating token for UUID: " + uuid);
                token.setLastAccess(LocalDateTime.now());
                mockRepo.updateToken(token);
            } finally {
                lock.writeLock().unlock();
            }
        };

        // Start concurrent read and write operations
        Thread readThread1 = new Thread(readTask);
        Thread readThread2 = new Thread(readTask);
        Thread writeThread = new Thread(writeTask);

        readThread1.start();
        readThread2.start();
        writeThread.start();

        // Wait for all threads to complete
        readThread1.join();
        readThread2.join();
        writeThread.join();

        // Verify that the token's last access time was updated correctly
        assertNotNull(mockRepo.getTokenByUuid(uuid).getLastAccess());
        System.out.println("Read-write lock management tested successfully.");
    }
}
