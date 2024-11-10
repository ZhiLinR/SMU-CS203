package matchmaking.config;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application configuration class for defining common beans.
 *
 * <p>
 * This class is annotated with {@code @Configuration}, indicating that it
 * provides
 * Spring beans for password encoding and database naming strategies.
 *
 * <p>
 * Beans:
 * <ul>
 * <li>{@link PhysicalNamingStrategy}: Configures the naming strategy for
 * mapping entity names to database table names, using the
 * {@link PhysicalNamingStrategyStandardImpl} implementation.</li>
 * </ul>
 */
@Configuration
public class AppConfig {

    /**
     * Provides a {@link PhysicalNamingStrategy} bean that defines the strategy
     * for mapping entity names to physical database table names.
     *
     * <p>
     * This implementation uses the {@link PhysicalNamingStrategyStandardImpl},
     * which follows Hibernate's default physical naming strategy.
     *
     * @return a {@link PhysicalNamingStrategy} instance for entity-to-table name
     *         mapping
     */
    @Bean
    public PhysicalNamingStrategy physical() {
        return new PhysicalNamingStrategyStandardImpl();
    }

    /**
     * Provides a {@link RestTemplate} bean for making HTTP requests to other
     * microservices or external APIs.
     *
     * <p>
     * The {@link RestTemplate} bean is commonly used for synchronous REST calls,
     * enabling easy communication with other services in a microservices
     * architecture. The bean is managed by Spring's application context, allowing
     * it to be autowired into any service class where HTTP requests are needed.
     *
     * @return a {@link RestTemplate} instance for making HTTP requests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
