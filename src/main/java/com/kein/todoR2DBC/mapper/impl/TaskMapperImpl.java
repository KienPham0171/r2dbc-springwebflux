package com.kein.todoR2DBC.mapper.impl;

import com.kein.todoR2DBC.domain.Tag;
import com.kein.todoR2DBC.domain.Task;
import com.kein.todoR2DBC.domain.TaskRequest;
import com.kein.todoR2DBC.mapper.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class TaskMapperImpl implements TaskMapper {
    @Override
    public Task taskRequestToTask(TaskRequest taskRequest) {
        System.err.println(taskRequest.toString());
        Task result = new Task();
        if(taskRequest.getId()!=0) result.setId(taskRequest.getId());
        result.setName(taskRequest.getName());
        if(taskRequest.getStatus()!= null) {
            result.setStatus(taskRequest.getStatus());}
        else{
            result.setStatus("not_started");
        }
        List<Tag> tagList = taskRequest.getSetTags().stream().flatMap(tagId -> {
            return Stream.of(new Tag(tagId));
        }).toList();
        result.setTags(tagList);
        System.out.println(result);
        return result;
    }
}
