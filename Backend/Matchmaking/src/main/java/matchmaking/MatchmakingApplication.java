package matchmaking;

import org.springframework.boot.SpringApplication;
import io.github.cdimascio.dotenv.Dotenv; // For mvn run
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point for the Matchmaking application.
 *
 * <p>
 * This class contains the main method which serves as the starting point for
 * the Spring Boot application.
 * It loads environment variables from a .env file to configure database.
 * </p>
 */
@SpringBootApplication
public class MatchmakingApplication {

    /**
     * The main method that runs the application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {

        // Load the .env file (For mvn run)
        Dotenv dotenv = Dotenv.configure()
                .load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        SpringApplication.run(MatchmakingApplication.class, args);
    }
}
