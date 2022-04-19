package com.kein.todoR2DBC.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("HEROES1")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Heroes1 {
    @Id
    private long id;
    @Column("NAME")
    private String name;
}
