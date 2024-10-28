package matchmaking.config;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class for defining common beans.
 *
 * <p>This class is annotated with {@code @Configuration}, indicating that it provides
 * Spring beans for password encoding and database naming strategies.
 *
 * <p>Beans:
 * <ul>
 *   <li>{@link PhysicalNamingStrategy}: Configures the naming strategy for
 *   mapping entity names to database table names, using the
 *   {@link PhysicalNamingStrategyStandardImpl} implementation.</li>
 * </ul>
 */
@Configuration
public class AppConfig {

    /**
     * Provides a {@link PhysicalNamingStrategy} bean that defines the strategy
     * for mapping entity names to physical database table names.
     *
     * <p>This implementation uses the {@link PhysicalNamingStrategyStandardImpl},
     * which follows Hibernate's default physical naming strategy.
     *
     * @return a {@link PhysicalNamingStrategy} instance for entity-to-table name mapping
     */
    @Bean
    public PhysicalNamingStrategy physical() {
        return new PhysicalNamingStrategyStandardImpl();
    }
}
