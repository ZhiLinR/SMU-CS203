package matchmaking.repository;

import matchmaking.model.Tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for performing database operations related to
 * tournaments.
 */
public interface TournamentRepository extends JpaRepository<Tournament, String> {

    /**
     * Retrieves a tournament by its ID.
     * 
     * @param tournamentId the ID of the tournament to retrieve
     * @return the tournament entity
     */
    @Procedure(name = "GetTournamentById")
    Tournament getTournamentById(@Param("p_tournamentId") String tournamentId);

}