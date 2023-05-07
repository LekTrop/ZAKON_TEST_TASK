package ua.zhytariuk.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Objects;

/**
 * Represent question model
 *
 * @author (ozhytary)
 */
@Document(indexName = "questions")
public class Question {

    public static final String TEXT_KEYWORD_SUFFIX = "keyword";
    public static final String TEXT_FIELD = "text";

    @Id
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, name = TEXT_FIELD),
            otherFields = @InnerField(suffix = TEXT_KEYWORD_SUFFIX, type = FieldType.Keyword)
    )
    private String text;

    public Question() {

    }

    public Question(final String question) {
        this.text = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) && Objects.equals(text, question.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
