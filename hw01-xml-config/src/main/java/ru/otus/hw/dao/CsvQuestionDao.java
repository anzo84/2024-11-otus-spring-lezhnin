package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    public static final String TEST_FILE_NOT_FOUND_MSG = "Test %s file not found";

    public static final String CANT_READ_FILE_MSG = "Can't %s read test file";

    public static final int SKIP_LINES = 1;

    public static final char SEPARATOR = ';';

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/
        String testFileName = fileNameProvider.getTestFileName();
        InputStream inputStream = Optional.ofNullable(getClass().getClassLoader()
            .getResourceAsStream(testFileName))
            .orElseThrow(() -> new QuestionReadException(String.format(TEST_FILE_NOT_FOUND_MSG, testFileName)));

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            return getQuestionsFrom(inputStreamReader)
                .stream()
                .map(QuestionDto::toDomainObject)
                .collect(Collectors.toList());

        } catch (IOException e) {
          throw new QuestionReadException(String.format(CANT_READ_FILE_MSG, testFileName), e);
        }
    }

    private List<QuestionDto> getQuestionsFrom(InputStreamReader inputStreamReader) {
        return new CsvToBeanBuilder<QuestionDto>(inputStreamReader)
            .withType(QuestionDto.class)
            .withSkipLines(SKIP_LINES)
            .withSeparator(SEPARATOR)
            .build()
            .parse();
    }
}
