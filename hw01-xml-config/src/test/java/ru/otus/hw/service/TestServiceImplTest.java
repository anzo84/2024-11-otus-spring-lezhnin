package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

  TestService subject;

  @Mock
  QuestionDao questionDao;

  @Mock
  IOService ioService;

  @Captor
  ArgumentCaptor<String> printLineCaptor;

  @BeforeEach
  void setUp() {
    subject = new TestServiceImpl(ioService, questionDao);
    List<Answer> answers = List.of(new Answer("a", false), new Answer("b", true), new Answer("c", false));
    List<Question> questions = List.of(new Question("question", answers));
    when(questionDao.findAll()).thenReturn(questions);
  }

  @Test
  public void testCorrectExecuteTestOutput() {
    subject.executeTest();
    verify(ioService, times(1)).printFormattedLine(printLineCaptor.capture());
    assertThat(printLineCaptor.getAllValues())
        .isNotEmpty()
        .containsSequence("Please answer the questions below%n");

    verify(ioService, times(6)).printLine(printLineCaptor.capture());
    assertThat(printLineCaptor.getAllValues())
        .isNotEmpty()
        .containsSequence("", "question", "1) a", "2) b", "3) c", "");
  }
}