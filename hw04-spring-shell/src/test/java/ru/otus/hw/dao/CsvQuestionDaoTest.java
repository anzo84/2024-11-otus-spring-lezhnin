package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @Test
    public void testFindAll() {
        List<Question> questions = questionDao.findAll();
        assertThat(questions)
            .hasSize(4)
            .map(x -> x.answers().size())
            .containsSequence(3, 4, 3, 2);

        assertThat(questions)
            .flatMap(Question::answers)
            .map(Answer::text)
            .containsSequence("Q1-A1", "Q1-A2", "Q1-A3",
                "Q2-A1", "Q2-A2", "Q2-A3", "Q2-A4",
                "Q3-A1", "Q3-A2", "Q3-A3",
                "Q4-A1", "Q4-A2");

        assertThat(questions)
            .map(Question::text)
            .containsSequence("Question 1", "Question 2", "Question 3", "Question 4");

        assertThat(questions)
            .map(this::getIndexOfTrueAnswer)
            .containsSequence(0, 1, 2, 0);
    }

    private int getIndexOfTrueAnswer(Question questions) {
        for (int i = 0; i < questions.answers().size(); i++) {
            if (questions.answers().get(i).isCorrect()) {
                return i;
            }
        }
        return -1;
    }
}