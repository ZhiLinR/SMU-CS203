package middlewareapd;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import middlewareapd.model.JWToken;
import middlewareapd.service.MiddlewareService;
import middlewareapd.repository.MockJWTRepository;
import middlewareapd.exception.UnauthorizedException;

public class Main {

    public static void main(String[] args) {
        // Initialize MockJWTRepository
        MockJWTRepository mockRepository = new MockJWTRepository();

        // Initialize MiddlewareService with the mock repository
        MiddlewareService middlewareService = new MiddlewareService(mockRepository);

        // Get all mock tokens from the repository
        List<JWToken> mockTokens = mockRepository.getAllTokens();

        // Run validations with threads and time it
        long threadedTime = runValidationWithThreads(mockTokens, middlewareService);

        // Run validations sequentially and time it
        long sequentialTime = runValidationSequentially(mockTokens, middlewareService);

        System.out.println("Time taken for threaded validation: " + threadedTime + " ms");
        System.out.println("Time taken for sequential validation: " + sequentialTime + " ms");
    }

    /**
     * Runs validation on a list of JWT tokens using multiple threads.
     *
     * @param tokens the list of JWT tokens to validate.
     * @param middlewareService the MiddlewareService used for validation.
     * @return the time taken to complete the validation, in milliseconds.
     */
    private static long runValidationWithThreads(List<JWToken> tokens, MiddlewareService middlewareService) {
        long startTime = System.currentTimeMillis();

        // Create a fixed thread pool to process tokens concurrently
        ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust as needed

        System.out.println("Validating Mock Tokens with Threads:");

        // Submit a validation task for each token
        for (JWToken token : tokens) {
            executorService.submit(() -> {
                try {
                    validateToken(token, middlewareService);
                } catch (Exception e) {
                    System.err.println("Error validating token: " + token.getUuid() + " - " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor service gracefully
        shutdownExecutorService(executorService);

        return System.currentTimeMillis() - startTime;
    }

    /**
     * Validates tokens sequentially using the specified MiddlewareService and returns the time taken.
     *
     * @param tokens the list of JWT tokens to validate.
     * @param middlewareService the service used for JWT validation.
     * @return the time taken for the validation in milliseconds.
     */
    private static long runValidationSequentially(List<JWToken> tokens, MiddlewareService middlewareService) {
        long startTime = System.currentTimeMillis();

        System.out.println("Validating Mock Tokens Sequentially:");

        for (JWToken token : tokens) {
            validateToken(token, middlewareService);
        }

        return System.currentTimeMillis() - startTime;
    }

    /**
     * Validates a single token and prints the result.
     *
     * @param token the JWToken to validate.
     * @param middlewareService the service used for JWT validation.
     */
    private static void validateToken(JWToken token, MiddlewareService middlewareService) {
        try {
            // Attempt to validate the token's JWT
            Map<String, Object> result = middlewareService.checkJwt(token.getJwt());

            // If successful, print the result
            System.out.println("Token valid. UUID: " + result.get("uuid") + ", isAdmin: " + result.get("isAdmin"));
        } catch (UnauthorizedException e) {
            System.out.println("Invalid token: " + token.getJwt() + ". Reason: " + e.getMessage());
        }
    }

    /**
     * Shuts down the executor service gracefully, waiting for tasks to complete.
     *
     * @param executorService the executor service to shut down.
     */
    private static void shutdownExecutorService(ExecutorService executorService) {
        executorService.shutdown(); // Initiate shutdown

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate in the specified time. Forcing shutdown...");
                
                // Force shutdown and log unfinished tasks
                List<Runnable> unfinishedTasks = executorService.shutdownNow();
                System.err.println("Unfinished tasks: " + unfinishedTasks.size());
            }
        } catch (InterruptedException e) {
            System.err.println("Shutdown interrupted. Forcing shutdown...");
            executorService.shutdownNow(); // Force shutdown if interrupted
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
}
