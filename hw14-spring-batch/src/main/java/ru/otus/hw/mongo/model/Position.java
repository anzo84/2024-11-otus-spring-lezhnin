package ru.otus.hw.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @EqualsAndHashCode.Include
    @Field("id")
    private String positionId;

    @Field("product_name")
    private String productName;

    @Field("uom")
    private String uom;

    @Field("quantity")
    private BigDecimal quantity;

    @Field("price")
    private BigDecimal price;
}