package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Student;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TestServiceImplTest {

    @TestConfiguration
    @ComponentScan(basePackages = {"ru.otus.hw.service", "ru.otus.hw.dao"})
    static class TestContextConfiguration {
    }

    @Autowired
    private TestService testService;

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private AppProperties appProperties;

    @Test
    public void testCorrectExecuteTestOutput() {
        ArgumentCaptor<String> printLineCaptor = ArgumentCaptor.forClass(String.class);
        Student student = new Student("Ivanov", "Ivan");

        when(appProperties.getTestFileName()).thenReturn("questions.csv");
        when(appProperties.getRightAnswersCountToPass()).thenReturn(3);
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);

        testService.executeTestFor(student);

        verify(ioService, times(1)).printLineLocalized(printLineCaptor.capture());
        verify(ioService, times(17)).printLine(printLineCaptor.capture());
        assertThat(printLineCaptor.getAllValues())
            .isNotEmpty()
            .containsSequence("TestService.answer.the.questions", "",
                "Question 1", "1) Q1-A1", "2) Q1-A2", "3) Q1-A3",
                "Question 2", "1) Q2-A1", "2) Q2-A2", "3) Q2-A3", "4) Q2-A4",
                "Question 3", "1) Q3-A1", "2) Q3-A2", "3) Q3-A3",
                "Question 4", "1) Q4-A1", "2) Q4-A2");
    }
}