package TournamentAdminService.repository;

import TournamentAdminService.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, String> {
    @Procedure(name = "CreateTournament")
    void createTournament(@Param("p_name") String name, @Param("p_startDate") Date startDate,
                          @Param("p_endDate") Date endDate, @Param("p_location") String location,
                          @Param("p_playerLimit") int playerLimit, @Param("p_isActive") boolean isActive,
                          @Param("p_descOID") String descOID);

    @Procedure(name = "UpdateTournament")
    void updateTournament(@Param("p_tournamentId") String tournamentId, @Param("p_name") String name,
                          @Param("p_startDate") Date startDate, @Param("p_endDate") Date endDate,
                          @Param("p_location") String location, @Param("p_playerLimit") int playerLimit,
                          @Param("p_isActive") boolean isActive, @Param("p_descOID") String descOID);

    @Procedure(name = "DeleteTournament")
    void deleteTournament(@Param("p_tournamentId") String tournamentId);

    @Procedure(name = "GetTournamentById")
    Tournament getTournamentById(@Param("p_tournamentId") String tournamentId);

    @Procedure(name = "GetAllTournaments")
    List<Tournament> getAllTournaments();
}