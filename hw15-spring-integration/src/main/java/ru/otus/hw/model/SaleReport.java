package ru.otus.hw.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SaleReport {

    private LocalDate date;

    private List<SaleReportPosition> reportPositions;
}
