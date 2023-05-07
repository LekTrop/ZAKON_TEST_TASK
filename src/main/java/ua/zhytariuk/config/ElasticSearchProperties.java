package ua.zhytariuk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * A {@link ConfigurationProperties} bean for ElasticSearch
 *
 * @author (ozhytary)
 */
@Component
public class ElasticSearchProperties {

    private String url;
    private Integer timeoutSocket;
    private Integer timeoutConnection;
    private Integer minSimilarityWordLength;

    public ElasticSearchProperties(final @Value("${elasticsearch.url}") String url,
                                   final @Value("${elasticsearch.timeout.socket}") Integer socket,
                                   final @Value("${elasticsearch.timeout.connection}") Integer connection,
                                   final @Value("${elasticsearch.search.similarity.word.length.min}") Integer min) {
        this.url = url;
        this.timeoutSocket = socket;
        this.timeoutConnection = connection;
        this.minSimilarityWordLength = min;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Integer getTimeoutSocket() {
        return timeoutSocket;
    }

    public void setTimeoutSocket(final Integer timeoutSocket) {
        this.timeoutSocket = timeoutSocket;
    }

    public Integer getTimeoutConnection() {
        return timeoutConnection;
    }

    public void setTimeoutConnection(final Integer timeoutConnection) {
        this.timeoutConnection = timeoutConnection;
    }

    public Integer getMinSimilarityWordLength() {
        return minSimilarityWordLength;
    }

    public void setMinSimilarityWordLength(final Integer minSimilarityWordLength) {
        this.minSimilarityWordLength = minSimilarityWordLength;
    }
}
