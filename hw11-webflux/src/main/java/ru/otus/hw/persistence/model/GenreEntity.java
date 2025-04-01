package ru.otus.hw.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("genres")
public class GenreEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;
}