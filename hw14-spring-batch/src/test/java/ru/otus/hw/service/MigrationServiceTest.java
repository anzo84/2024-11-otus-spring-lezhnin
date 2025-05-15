package ru.otus.hw.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.mongo.model.Position;
import ru.otus.hw.mongo.model.Receipt;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@SpringBootTest
@Import({MigrateServiceImpl.class})
@DisplayName("Тест сервиса миграции")
public class MigrationServiceTest {

    public static final Tuple[] TUPLES = {tuple("Ноутбук", "шт", 1.00, 120000.00),
        tuple("Мышь", "шт", 1.00, 2500.00),
        tuple("Коврик для мыши", "шт", 1.00, 1500.00),
        tuple("Хлеб", "шт", 2.00, 50.00),
        tuple("Молоко", "л", 1.00, 80.00),
        tuple("Яйца", "дес", 1.00, 120.00),
        tuple("Сыр", "кг", 0.30, 600.00),
        tuple("Гвозди", "кг", 0.50, 300.00),
        tuple("Доска", "шт", 3.00, 500.00),
        tuple("Краска", "банка", 2.00, 700.00),
        tuple("Кисть", "шт", 3.00, 150.00),
        tuple("Шурупы", "уп", 1.00, 250.00),
        tuple("Кофе", "кг", 0.20, 1200.00),
        tuple("Чай", "уп", 2.00, 300.00),
        tuple("Печенье", "уп", 3.00, 150.00),
        tuple("Вода", "бут", 6.00, 80.00),
        tuple("Сок", "бут", 2.00, 120.00),
        tuple("Шоколад", "шт", 5.00, 90.00)};

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("Должен проверить миграцию данных в mongo")
    @Test
    void shouldMigrateData() {
        List<Receipt> receiptList = mongoTemplate.findAll(Receipt.class);
        assertThat(receiptList)
            .isNotEmpty()
            .hasSize(5)
            .flatMap(Receipt::getPositions)
            .extracting(Position::getProductName, Position::getUom,
                p -> p.getQuantity().doubleValue(),
                p -> p.getPrice().doubleValue())
            .containsExactlyInAnyOrder(
                TUPLES
            );
    }
}