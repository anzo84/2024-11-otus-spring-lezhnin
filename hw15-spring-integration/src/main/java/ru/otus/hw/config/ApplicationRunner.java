package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.integration.PointOfSaleGateway;
import ru.otus.hw.model.CloseDay;
import ru.otus.hw.model.ReceiptPosition;
import ru.otus.hw.util.Cashier;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private static final ReceiptPosition[] RECEIPT_POSITIONS = new ReceiptPosition[]{
        Cashier.sale("1", "Товар1", 1.0, "ШТ", 20.0, 0.0, false),
        Cashier.sale("1", "Товар2", 2.0, "ШТ", 30.0, 0.0, false),
        Cashier.sale("1", "Товар3", 1.0, "ШТ", 50.0, 0.0, false),
        Cashier.sale("1", "Товар4", 3.0, "ШТ", 100.0, 0.0, true),
        Cashier.sale("2", "Товар1", 2.0, "ШТ", 20.0, 0.0, false),
        Cashier.sale("2", "Товар2", 1.0, "ШТ", 30.0, 0.0, false),
        Cashier.sale("2", "Товар3", 4.0, "ШТ", 50.0, 0.0, false),
        Cashier.sale("2", "Товар4", 1.0, "ШТ", 100.0, 0.0, true),
    };

    private final PointOfSaleGateway pointOfSaleGateway;

    @Override
    public void run(String... args) {
        Arrays.stream(RECEIPT_POSITIONS).forEach(pointOfSaleGateway::registerOperation);
        pointOfSaleGateway.registerOperation(new CloseDay(LocalDate.now()));
    }

}
