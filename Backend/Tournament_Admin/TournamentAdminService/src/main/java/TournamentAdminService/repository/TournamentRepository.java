package TournamentAdminService.repository;

import TournamentAdminService.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

/**
 * Repository interface for performing database operations related to tournaments.
 */
public interface TournamentRepository extends JpaRepository<Tournament, String> {

     /**
     * Creates a new tournament.
     * 
     * @param name       the name of the tournament
     * @param startDate  the start date of the tournament
     * @param endDate    the end date of the tournament
     * @param location   the location of the tournament
     * @param playerLimit the player limit for the tournament
     * @param isActive   the active status of the tournament
     * @param descOID    the description OID for the tournament
     */
    @Procedure(name = "CreateTournament")
    void createTournament(@Param("p_name") String name, @Param("p_startDate") Date startDate,
                          @Param("p_endDate") Date endDate, @Param("p_location") String location,
                          @Param("p_playerLimit") int playerLimit, @Param("p_isActive") boolean isActive,
                          @Param("p_descOID") String descOID);

                          
    /**
     * Updates an existing tournament.
     * 
     * @param tournamentId the ID of the tournament to update
     * @param name         the updated name of the tournament
     * @param startDate    the updated start date of the tournament
     * @param endDate      the updated end date of the tournament
     * @param location     the updated location of the tournament
     * @param playerLimit  the updated player limit for the tournament
     * @param isActive     the updated active status of the tournament
     * @param descOID      the updated description OID for the tournament
     */
    @Procedure(name = "UpdateTournament")
    void updateTournament(@Param("p_tournamentId") String tournamentId, @Param("p_name") String name,
                          @Param("p_startDate") Date startDate, @Param("p_endDate") Date endDate,
                          @Param("p_location") String location, @Param("p_playerLimit") int playerLimit,
                          @Param("p_isActive") boolean isActive, @Param("p_descOID") String descOID);


    /**
     * Deletes an existing tournament by its ID.
     * 
     * @param tournamentId the ID of the tournament to delete
     */
    @Procedure(name = "DeleteTournament")
    void deleteTournament(@Param("p_tournamentId") String tournamentId);

    /**
     * Retrieves a tournament by its ID.
     * 
     * @param tournamentId the ID of the tournament to retrieve
     * @return the tournament entity
     */
    @Procedure(name = "GetTournamentById")
    Tournament getTournamentById(@Param("p_tournamentId") String tournamentId);

    /**
     * Retrieves all tournaments in the system.
     * 
     * @return a list of all tournaments
     */
    @Procedure(name = "GetAllTournaments")
    List<Tournament> getAllTournaments();
}