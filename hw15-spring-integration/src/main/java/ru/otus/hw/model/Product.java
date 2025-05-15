package ru.otus.hw.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    private String productName;

    private String uom;

    private BigDecimal price;
}
