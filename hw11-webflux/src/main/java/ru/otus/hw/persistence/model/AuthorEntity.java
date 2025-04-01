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
@Table("authors")
public class AuthorEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("full_name")
    private String fullName;
}