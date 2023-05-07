package ua.zhytariuk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provide configuration for question service
 *
 * @author (ozhytary)
 */
@Configuration
public class QuestionExecutorServiceConfiguration {

    @Bean
    public ExecutorService getExecutorService(final @Value("${executor.elasticsearch.threads.count}") int threadsCount) {
        return Executors.newFixedThreadPool(threadsCount);
    }
}
