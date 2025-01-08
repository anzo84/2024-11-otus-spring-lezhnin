package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    QuestionDao subject;

    @Mock
    TestFileNameProvider fileNameProvider;

    @BeforeEach
    public void setUp() {
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
        subject = new CsvQuestionDao(fileNameProvider);
    }

    @Test
    public void testFindAll() {
        List<Question> questions = subject.findAll();
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