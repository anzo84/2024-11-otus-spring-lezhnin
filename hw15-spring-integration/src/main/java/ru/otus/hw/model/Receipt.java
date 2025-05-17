package ru.otus.hw.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class Receipt {

    private LocalDateTime dateTime;

    private String id;

    private List<ReceiptPosition> receiptPositions;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
            sb.append("\n").
                append("Чек №")
                .append(id)
                .append(" ")
                .append(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .append("\n");
        receiptPositions.forEach(p -> sb.append("  ").append(p.toString()).append("\n"));
        return sb.toString();
    }

}
