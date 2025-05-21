package ru.otus.hw.integration;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.integration.store.MessageGroup;
import org.springframework.integration.store.SimpleMessageGroup;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import ru.otus.hw.model.Receipt;
import ru.otus.hw.model.ReceiptPosition;
import ru.otus.hw.model.SaleReport;
import ru.otus.hw.model.SaleReportPosition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Тесты для ReceiptMessageGroupProcessor")
public class ReceiptMessageGroupProcessorTest {

    private static final LocalDate TEST_DATE = LocalDate.now();

    private static final String MILK = "Молоко";

    private static final String BREAD = "Хлеб";

    private static final String WATER = "Вода";

    private static final String APPLES = "Яблоки";

    private final ReceiptMessageGroupProcessor processor = new ReceiptMessageGroupProcessor();

    @Test
    @DisplayName("Должен вернуть пустой отчет при обработке пустой группы сообщений")
    public void shouldReturnEmptyReportWhenProcessingEmptyMessageGroup() {
        MessageGroup group = mockEmptyMessageGroup();
        SaleReport result = (SaleReport) processor.processMessageGroup(group);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Должен правильно обработать один чек с несколькими позициями")
    public void shouldProcessSingleReceiptWithMultiplePositionsCorrectly() {
        ReceiptPosition milkPosition = createPosition("1", MILK, 2, 100, 10, "шт", false);
        ReceiptPosition breadPosition = createPosition("1", BREAD, 1, 200, 0, "кг", true);

        Message<Receipt> message = createReceiptMessage("1", TEST_DATE, milkPosition, breadPosition);
        MessageGroup group = new SimpleMessageGroup(List.of(message), "group1");

        assertReportValid((SaleReport) processor.processMessageGroup(group), TEST_DATE,
            tuple(MILK, 2.0, "шт", 190.0),
            tuple(BREAD, 1.0, "кг", 200.0)
        );
    }

    @Test
    @DisplayName("Должен агрегировать количество и сумму для одинаковых товаров из разных чеков")
    void shouldAggregateQuantitiesAndTotalsForSameProductsFromDifferentReceipts() {
        ReceiptPosition applesPos1 = createPosition("1", APPLES, 2, 100, 10, "кг", false);
        ReceiptPosition applesPos2 = createPosition("2", APPLES, 3, 100, 20, "кг", false);

        MessageGroup group = createMessageGroup(
            createReceiptMessage("1", TEST_DATE, applesPos1),
            createReceiptMessage("2", TEST_DATE, applesPos2)
        );

        assertReportValid((SaleReport) processor.processMessageGroup(group), TEST_DATE,
            tuple(APPLES, 5.0, "кг", 470.0)
        );
    }

    @Test
    @DisplayName("Должен обрабатывать несколько разных товаров из разных чеков")
    void shouldProcessMultipleDifferentProductsFromDifferentReceipts() {
        ReceiptPosition milkPosition = createPosition("1", MILK, 2, 100, 10, "шт", false);
        ReceiptPosition breadPosition = createPosition("1", BREAD, 2, 200, 0, "кг", false);
        ReceiptPosition waterPosition = createPosition("2", WATER, 5, 50, 5, "л", true);

        MessageGroup group = createMessageGroup(
            createReceiptMessage("1", TEST_DATE, milkPosition, breadPosition),
            createReceiptMessage("2", TEST_DATE, waterPosition)
        );

        assertReportValid((SaleReport) processor.processMessageGroup(group), TEST_DATE,
            tuple(MILK, 2.0, "шт", 190.0),
            tuple(BREAD, 2.0, "кг", 400.0),
            tuple(WATER, 5.0, "л", 245.0)
        );
    }

    // ======= Вспомогательные методы =======

    private MessageGroup mockEmptyMessageGroup() {
        MessageGroup group = mock(MessageGroup.class);
        when(group.getMessages()).thenReturn(Collections.emptyList());
        return group;
    }

    private ReceiptPosition createPosition(Object... args) {
        return new ReceiptPosition(
            (String) args[0],
            (String) args[1],
            BigDecimal.valueOf((int) args[2]),
            (String) args[5],
            BigDecimal.valueOf((int) args[3]),
            BigDecimal.valueOf((int) args[4]),
            (boolean) args[6]
        );
    }

    private Message<Receipt> createReceiptMessage(String receiptId, LocalDate reportDate,
                                                  ReceiptPosition... positions) {
        Receipt receipt = new Receipt();
        receipt.setId(receiptId);
        receipt.setDateTime(LocalDateTime.now());
        receipt.setReceiptPositions(Arrays.asList(positions));
        return new GenericMessage<>(receipt, Map.of("report-date", reportDate));
    }

    private MessageGroup createMessageGroup(Message<Receipt>... messages) {
        return new SimpleMessageGroup(Arrays.asList(messages), UUID.randomUUID().toString());
    }

    private static Tuple extractReportPositionToTuple(SaleReportPosition position) {
        return tuple(
            position.getProductName(),
            position.getQuantity().doubleValue(),
            position.getUom(),
            position.getTotal().doubleValue()
        );
    }

    private void assertReportValid(SaleReport report, LocalDate expectedDate, Tuple... expectedPositions) {
        assertThat(report)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.getDate()).isEqualTo(expectedDate);
                assertThat(r.getReportPositions())
                    .hasSize(expectedPositions.length)
                    .map(ReceiptMessageGroupProcessorTest::extractReportPositionToTuple)
                    .containsExactlyInAnyOrder(expectedPositions);
            });
    }
}