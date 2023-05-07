package ua.zhytariuk.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ua.zhytariuk.model.Question;

/**
 * Repository to interact with persistence layer to store {@link Question} data
 *
 * @author (ozhytary)
 */
public interface QuestionRepository extends ElasticsearchRepository<Question, String> {
}
