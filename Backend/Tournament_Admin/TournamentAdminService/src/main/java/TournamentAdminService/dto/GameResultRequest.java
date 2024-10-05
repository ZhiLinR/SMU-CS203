package TournamentAdminService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResultRequest {
    private String tournamentID;
    private Integer roundNum;
    private String playerWon;
}
