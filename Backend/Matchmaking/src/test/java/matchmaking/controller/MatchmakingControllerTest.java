package matchmaking.controller;

import matchmaking.MatchmakingApplication;
import matchmaking.service.*;
import matchmaking.model.*;
import matchmaking.exception.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MatchmakingApplication.class)
@AutoConfigureMockMvc
/**
 * Integration tests for the MatchmakingController class,
 * which handles matchmaking operations.
 */
public class MatchmakingControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private MatchingService matchingService;

        /**
         * Sets up environment properties before any tests are executed.
         * <p>
         * Uses the <a href="https://github.com/cdimascio/dotenv-java">Dotenv</a>
         * library to load environment variables and sets system properties required for
         * the middleware.
         * </p>
         */
        @BeforeAll
        public static void setUpEnvironment() {
                Dotenv dotenv = Dotenv.configure().load();
                System.setProperty("DB_URL", dotenv.get("DB_URL"));
                System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
                System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        }

        /**
         * Tests the health check endpoint for a successful response.
         * <p>
         * This test performs a GET request to the health check endpoint and verifies
         * that
         * the response status is OK (200), the content type is JSON, and the response
         * contains the expected success message.
         * </p>
         * 
         * @throws Exception if an error occurs during the request execution or response
         *                   validation.
         */
        @Test
        public void testHealthCheck_Success() throws Exception {
                mockMvc.perform(get("/api/health"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Health Check Success"));
        }

        /**
         * Tests the matchmaking functionality for a successful response.
         * <p>
         * This test simulates a scenario where valid player data is provided,
         * and it verifies that the matchmaking service returns the expected response.
         * </p>
         * 
         * @throws Exception if an error occurs during the request execution or response
         *                   validation.
         */
        @Test
        public void testMatchPlayers_Success() throws Exception {
                String tournamentId = "123";

                // Create and set up a MatchupsId instance
                MatchupsId mockMatchupsId = new MatchupsId();
                mockMatchupsId.setPlayer1("player1");
                mockMatchupsId.setPlayer2("player2");
                mockMatchupsId.setTournamentId(tournamentId);

                // Create and set up a Matchups instance
                Matchups mockMatchup = new Matchups();
                mockMatchup.setId(mockMatchupsId);
                mockMatchup.setPlayerWon("player1");
                mockMatchup.setRoundNum(1);

                // Use Collections.singletonList to create a list with the mock Matchups
                // instance
                List<Matchups> mockMatchups = Collections.singletonList(mockMatchup);

                // Mock the behavior of matchingService to return the mock list
                Mockito.when(matchingService.generateUniqueMatchups(tournamentId)).thenReturn(mockMatchups);

                // Perform the request and verify the response
                mockMvc.perform(get("/api/matchmaking/{tournamentId}", tournamentId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Players matched successfully"));
        }

        /**
         * Tests the matchmaking functionality when the tournament is not found.
         * <p>
         * This test simulates a scenario where an invalid tournament ID is provided,
         * and it verifies that the appropriate error response is returned.
         * </p>
         * 
         * @throws Exception if an error occurs during the request execution or response
         *                   validation.
         */
        @Test
        public void testMatchPlayers_TournamentNotFound() throws Exception {
                String tournamentId = "invalid";
                Mockito.when(matchingService.generateUniqueMatchups(tournamentId))
                                .thenThrow(new TournamentNotFoundException("Tournament not found."));

                mockMvc.perform(get("/api/matchmaking/{tournamentId}", tournamentId))
                                .andExpect(status().isNotFound())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Tournament not found."));
        }

        /**
         * Tests the matchmaking functionality when an invalid argument is provided.
         * <p>
         * This test simulates a scenario where an empty tournament ID is provided,
         * and it verifies that the appropriate error response is returned.
         * </p>
         * 
         * @throws Exception if an error occurs during the request execution or response
         *                   validation.
         */
        @Test
        public void testMatchPlayers_InvalidArgument() throws Exception {
                String tournamentId = "";

                Mockito.when(matchingService.generateUniqueMatchups(tournamentId))
                                .thenThrow(new IllegalArgumentException("TournamentID must not be null or empty."));

                mockMvc.perform(get("/api/matchmaking/{tournamentId}", tournamentId))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("TournamentID must not be null or empty."));
        }

        /**
         * Tests the matchmaking functionality when an internal server error occurs.
         * <p>
         * This test simulates a scenario where an unexpected error occurs during the
         * matchmaking process, and it verifies that the appropriate error response is
         * returned.
         * </p>
         * 
         * @throws Exception if an error occurs during the request execution or response
         *                   validation.
         */
        @Test
        public void testMatchPlayers_InternalServerError() throws Exception {
                String tournamentId = "123";

                Mockito.when(matchingService.generateUniqueMatchups(tournamentId))
                                .thenThrow(new RuntimeException("Unexpected error occurred."));

                mockMvc.perform(get("/api/matchmaking/{tournamentId}", tournamentId))
                                .andExpect(status().isInternalServerError())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Error: Unexpected error occurred."));
        }
}