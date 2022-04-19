package com.kein.todoR2DBC.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


import java.util.List;

@Table("tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    private long id;
    @Column("name")
    private String name;

    @Transient
    List<Task> tasks;
    public Tag(long id){
        this.id = id;
    }
}
