package com.kein.todoR2DBC.mapper.impl;

import com.kein.todoR2DBC.domain.TagTask;
import com.kein.todoR2DBC.domain.Task;
import com.kein.todoR2DBC.mapper.TagTaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class TagTaskMapperImpl implements TagTaskMapper {
    @Override
    public List<TagTask> mapRealTaskToTagTask(Task task) {
        return task.getTags().stream()
                .map(tag -> new TagTask(tag.getId(),task.getId())).toList();
    }
}
