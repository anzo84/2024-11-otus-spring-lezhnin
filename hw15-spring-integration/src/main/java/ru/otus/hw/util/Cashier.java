package ru.otus.hw.util;

import ru.otus.hw.model.ReceiptPosition;

import java.math.BigDecimal;

public class Cashier {
    public static ReceiptPosition sale(Object... args) {
        return new ReceiptPosition(
            (String) args[0],
            (String) args[1],
            BigDecimal.valueOf((double) args[2]),
            (String) args[3],
            BigDecimal.valueOf((double) args[4]),
            BigDecimal.valueOf((double) args[5]),
            (boolean) args[6]);
    }
}
