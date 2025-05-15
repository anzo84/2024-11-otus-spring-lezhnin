package ru.otus.hw.service;

import org.springframework.integration.router.RecipientListRouter;
import ru.otus.hw.model.Product;
import ru.otus.hw.model.Receipt;
import ru.otus.hw.model.ReceiptPosition;
import ru.otus.hw.model.SaleReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис магазина
 */
public interface ShopService {

    /**
     * Формирует позицию чека
     * @param product - продукт
     * @return Возвращает позицию чека
     */
    ReceiptPosition makePosition(Product product, BigDecimal quantity);

    /**
     * Формирует чек продажи
     * @param positions - позиции чека
     * @param dateTime - дата/время формирования чека
     * @return чек продажи
     */
    Receipt makeReceipt(List<ReceiptPosition> positions, LocalDateTime dateTime);


    /**
     * Формирует отчет за дату
     *
     * @param receipts - список чеков
     * @param date - дата отчета
     * @return Отчет за дату
     */
    SaleReport makeSaleReport(List<Receipt> receipts, LocalDate date);
}
