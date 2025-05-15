package ru.otus.hw.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleReportPosition {

    private String productName;

    private String uom;

    private BigDecimal quantity = BigDecimal.ZERO;

    private BigDecimal total = BigDecimal.ZERO;
}