package ru.otus.hw.commands;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SetQuantityCommand implements Command {
    private BigDecimal quantity;
}