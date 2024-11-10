package TournamentAdminService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the Spring Boot application.
 * 
 * <p>This class is annotated with {@code @Configuration}, indicating that it defines 
 * beans to configure the security for the application. It customizes the 
 * {@link SecurityFilterChain} to disable CSRF protection and permit all HTTP requests 
 * without requiring authentication.
 * 
 * <p>Key methods:
 * <ul>
 *   <li>{@link #securityFilterChain(HttpSecurity)}: Configures the security filter chain 
 *   by disabling CSRF protection and allowing all requests.</li>
 * </ul>
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the {@link SecurityFilterChain} to disable CSRF protection 
     * and allow all requests without authentication.
     * 
     * @param http the {@link HttpSecurity} object used to configure security settings
     * @return the configured {@link SecurityFilterChain} object
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF protection
            .authorizeHttpRequests(authorize -> 
                authorize.anyRequest().permitAll()  // Allow all requests without authentication
            );
        
        return http.build();
    }
}



