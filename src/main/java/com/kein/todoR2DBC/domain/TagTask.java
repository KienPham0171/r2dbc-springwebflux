package com.kein.todoR2DBC.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("task_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagTask {
    @Id
    private long id;
    @Column("tag_id")
    private long tagId;
    @Column("task_id")
    private long taskId;

    public TagTask(long tagId, long taskId) {
        this.tagId = tagId;
        this.taskId = taskId;
    }
}
