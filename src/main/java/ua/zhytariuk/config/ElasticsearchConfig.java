package ua.zhytariuk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Provide elasticsearch configuration for the entire project
 *
 * @author (ozhytary)
 */
@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    private final ElasticSearchProperties elasticSearchProperties;

    public ElasticsearchConfig(final ElasticSearchProperties elasticSearchProperties) {
        this.elasticSearchProperties = elasticSearchProperties;
    }

    /**
     * Provide the {@link ClientConfiguration} for elasticsearch clients
     *
     * @return {@link ClientConfiguration}
     */
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                                  .connectedTo(elasticSearchProperties.getUrl())
                                  .withConnectTimeout(elasticSearchProperties.getTimeoutConnection())
                                  .withSocketTimeout(elasticSearchProperties.getTimeoutSocket())
                                  .build();
    }
}
