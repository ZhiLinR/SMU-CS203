package matchmaking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import user.dto.MatchingRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadocs
 */
@Service
public class MatchingService {

    /**
     * Matchmake users in tournament provided in the {@link MatchingRequest} 
     * based on ELO and round number.
     *
     * @param request the matching request containing the tournamentID.
     * @return {@code true} if the users successfully matched; {@code false} otherwise
     * @throws IllegalArgumentException if the provided profile request is invalid
     */
    @Transactional
    public void createProfile(MatchingRequest request) {

    }
}
