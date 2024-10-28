package TournamentAdminService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the API response returned from the server.
 * Contains a message and a success flag to indicate the outcome of an operation.
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private boolean isSuccessful;
    private Object data;

    public ApiResponse(String message, boolean isSucessful) {
        this.message = message;
        this.isSuccessful = isSucessful;
    }
    
}



