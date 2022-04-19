package com.kein.todoR2DBC.mapper;

import com.kein.todoR2DBC.domain.Task;
import com.kein.todoR2DBC.domain.TaskRequest;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapper {
    Task taskRequestToTask(TaskRequest taskRequest);
}
