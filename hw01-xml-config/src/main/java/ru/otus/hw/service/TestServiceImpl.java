package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.stream.IntStream;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    public static final String ANSWER_FORMAT = "%d) %s";

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        questionDao.findAll().forEach(this::printQuestion);
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        IntStream.range(0, question.answers().size())
            .forEach(index -> printAnswer(question, index));
        ioService.printLine("");
    }

    private void printAnswer(Question question, int index) {
        ioService.printLine(String.format(ANSWER_FORMAT, index + 1, question.answers().get(index).text()));
    }
}
