package com.kein.todoR2DBC.mapper;

import com.kein.todoR2DBC.domain.TagTask;
import com.kein.todoR2DBC.domain.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TagTaskMapper {
    List<TagTask> mapRealTaskToTagTask(Task task);
}
