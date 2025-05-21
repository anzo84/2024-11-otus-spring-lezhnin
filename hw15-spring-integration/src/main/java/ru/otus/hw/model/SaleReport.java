package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
public class SaleReport {

    private LocalDate date;

    private List<SaleReportPosition> reportPositions;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n Отчет ")
            .append(date.format(DateTimeFormatter.ISO_DATE))
            .append("\n");
        reportPositions.forEach(p -> sb.append("  ").append(p.toString()).append("\n"));
        return sb.toString();
    }

}
