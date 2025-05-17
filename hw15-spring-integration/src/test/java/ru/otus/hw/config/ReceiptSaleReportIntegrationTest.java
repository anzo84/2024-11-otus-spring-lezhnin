package ru.otus.hw.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.integration.PointOfSaleGateway;
import ru.otus.hw.model.CloseDay;
import ru.otus.hw.util.Cashier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@SpringIntegrationTest
@DirtiesContext
@ExtendWith(OutputCaptureExtension.class)
@DisplayName("Тест для проверки интегро потока")
public class ReceiptSaleReportIntegrationTest {

    @MockBean
    private ApplicationRunner runner;

    @Autowired
    private PointOfSaleGateway pointOfSaleGateway;

    @Test
    @DisplayName("Должен сформироваться отчет")
    void shouldProcessReceiptSaleReportFlow(CapturedOutput output) {
        LocalDate now = LocalDate.now();
        pointOfSaleGateway.registerOperation(Cashier.sale("1", "Товар1", 1.0, "ШТ", 20.0, 0.0, true));
        pointOfSaleGateway.registerOperation(Cashier.sale("2", "Товар1", 2.0, "ШТ", 10.0, 1.0, true));
        pointOfSaleGateway.registerOperation(new CloseDay(now));

        assertThat(output.getOut())
            .contains("Чек №1")
            .contains("Товар1 1.0 ШТ x 20.0 - 0.0 = 20.00")
            .contains("Чек №2")
            .contains("Товар1 2.0 ШТ x 10.0 - 1.0 = 19.00")
            .contains("Отчет " + now.format(DateTimeFormatter.ISO_DATE))
            .contains("Товар1 3.0 ШТ  39.00");
    }

}