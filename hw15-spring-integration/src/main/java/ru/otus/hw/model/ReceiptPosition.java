package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ReceiptPosition {

    private String receiptId;

    private String productName;

    private BigDecimal quantity;

    private String uom;

    private BigDecimal price;

    private BigDecimal discount;

    private Boolean isLast;

    public BigDecimal getTotal() {
        return quantity.multiply(price).subtract(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder =  new StringBuilder();
        stringBuilder.append(productName)
            .append(" ")
            .append(quantity)
            .append(" ")
            .append(uom)
            .append(" x ")
            .append(price)
            .append(" - ")
            .append(discount)
            .append(" = ")
            .append(getTotal());
        return stringBuilder.toString();
    }

}