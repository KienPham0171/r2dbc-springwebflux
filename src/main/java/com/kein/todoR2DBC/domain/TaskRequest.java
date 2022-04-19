package com.kein.todoR2DBC.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private long id;
    private String name;
    private String status;
    private Set<Long> setTags;
}
