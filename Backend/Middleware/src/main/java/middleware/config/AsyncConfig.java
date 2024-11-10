package middleware.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Number of core threads
        executor.setMaxPoolSize(50); // Maximum number of threads
        executor.setQueueCapacity(100); // Number of tasks in the queue
        executor.setThreadNamePrefix("async-executor-");
        executor.initialize();
        return executor;
    }
}