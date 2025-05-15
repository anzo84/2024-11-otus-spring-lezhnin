package ru.otus.hw.service;

import ru.otus.hw.model.Product;
import ru.otus.hw.model.ReceiptPosition;

import java.math.BigDecimal;

public interface ProductHandler {

    void handleProductSelection(Product product);

    ReceiptPosition handleQuantitySetting(BigDecimal quantity);
}
