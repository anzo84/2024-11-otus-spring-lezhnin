package ru.otus.hw.commands;

import lombok.Data;
import ru.otus.hw.model.Product;

@Data
public class SelectProductCommand implements Command {
    private Product product;
}
