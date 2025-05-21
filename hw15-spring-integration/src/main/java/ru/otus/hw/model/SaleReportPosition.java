package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SaleReportPosition {

    private String productName;

    private String uom;

    private BigDecimal quantity;

    private BigDecimal total;

    @Override
    public String toString() {
        StringBuilder stringBuilder =  new StringBuilder();
        stringBuilder.append(productName)
            .append(" ")
            .append(quantity)
            .append(" ")
            .append(uom)
            .append("  ")
            .append(total);
        return stringBuilder.toString();
    }
}