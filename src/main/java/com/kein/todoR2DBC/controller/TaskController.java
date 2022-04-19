package com.kein.todoR2DBC.controller;

import com.kein.todoR2DBC.domain.Task;
import com.kein.todoR2DBC.domain.TaskRequest;
import com.kein.todoR2DBC.mapper.TaskMapper;
import com.kein.todoR2DBC.repository.TaskRepository;
import com.kein.todoR2DBC.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@CrossOrigin
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskRepository taskRepository;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/tasks",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Task> getTask(){

        return taskService.getTasks();
    }
    @PostMapping("/tasks")
    public Mono<Task> createTask(@RequestBody TaskRequest tagRequest){
        return taskService.createTask(taskMapper.taskRequestToTask(tagRequest));
    }

    @GetMapping("/tasks/{id}")
    public Mono<Task> findTaskById(@PathVariable("id") long id){

        return taskService.findTaskById(id);

    }
    @DeleteMapping("/tasks/{id}")
    public Mono<Integer> delTaskById(@PathVariable("id") long id){
        return taskService.delTaskById(id).onErrorReturn(8);
    }

    @GetMapping(value = "/tasks/status/{status}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Task> findTaskByStatus(@PathVariable("status") String status){
        return taskService.findTasksByStatus(status);
    }
    @PutMapping("/tasks")
    public Mono<Task> updateTask(@RequestBody TaskRequest task){
        return taskService.updateTask(taskMapper.taskRequestToTask(task));
    }
    @GetMapping(value = "/tasks/tagId/{tagId}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Task> findAllTasksByTagId(@PathVariable("tagId") long id){
        return taskService.findTasksByTagId(id);
    }

}
