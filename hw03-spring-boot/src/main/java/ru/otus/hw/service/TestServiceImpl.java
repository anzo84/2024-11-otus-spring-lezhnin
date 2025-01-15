package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    public static final String ANSWER_FORMAT = "%d) %s";

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            printQuestion(question);
            int min = 1;
            int max = question.answers().size();
            int answerNumber = ioService.readIntForRangeWithPromptLocalized(min, max,
                "TestService.please.enter.the.answer.number",
                "TestService.input.error.enter.response.number.from.to");
            var isAnswerValid = question.answers().get(answerNumber - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        IntStream.range(0, question.answers().size()).forEach(index -> printAnswer(question, index));
    }

    private void printAnswer(Question question, int index) {
        ioService.printLine(String.format(ANSWER_FORMAT, index + 1, question.answers().get(index).text()));
    }
}