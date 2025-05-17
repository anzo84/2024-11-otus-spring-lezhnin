package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CloseDay {

    private LocalDate date;
}
