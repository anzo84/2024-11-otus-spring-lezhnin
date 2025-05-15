package ru.otus.hw.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceiptPosition {

    private String productName;

    private String uom;

    private BigDecimal quantity;

    private BigDecimal price;

    private BigDecimal discount;
}