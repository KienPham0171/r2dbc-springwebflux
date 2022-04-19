package com.kein.todoR2DBC.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {
    @Id
    private Long id;
    @Column("name")
    private String name;
    @Column("status")
    private String status;
    @Transient
    List<Tag> tags;

    public Task(String name, String status) {
        this.name = name;
        this.status = status;
    }
}
