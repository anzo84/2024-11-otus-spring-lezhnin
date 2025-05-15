package ru.otus.hw.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Receipt {

    private LocalDateTime dateTime;

    private List<ReceiptPosition> receiptPositions;

}
