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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

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

    @GetMapping(value = "/reactiveTasks",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Task> byReactive() throws InterruptedException {
//        return taskService.getTasks().delayElements(Duration.ofMillis(5000))
//                .doOnNext(task ->{
//                    System.err.println("Processing: "+ task.toString());
//                    System.out.println("ThreadId: " + Thread.currentThread().getId());
//                })
//                .doOnCancel(()-> System.out.println("Client has canceled request"))
//                .doOnComplete(()-> System.out.println("All items are processed"));
        System.out.println("ThreadId: "+ Thread.currentThread().getId());
        var st = Flux.range(10,50)
                .delayElements(Duration.ofMillis(3000))
                .map(item ->
                        new Task(Long.valueOf(item),"task: "+item,"status",List.of()))
                .doOnNext(task -> System.err.println("Processing: "+ task.toString()))
                .doOnComplete(()-> System.out.println("Completed"))
                .doOnCancel(()-> System.out.println("Cancel")).subscribe();
        Thread.sleep(10000);
        System.out.println("Do some task by threadId: " +Thread.currentThread().getId());

        return  Flux.range(1,5)
                .delayElements(Duration.ofMillis(3000))
                .map(item ->
                        new Task(Long.valueOf(item),"task: "+item,"status",List.of()));
//               .doOnNext(task -> System.err.println("Processing: "+ task.toString()))
//                .doOnCancel(()-> System.out.println("Cancel"))
//                .doOnComplete(()-> System.out.println("completed"));
    }

    @GetMapping(value = "/blockTasks")
    public List<Task> byBlockingIo() throws InterruptedException {
        long start = Calendar.getInstance().getTimeInMillis();
        List<Long> list = List.of(1l,2l,3l,4l,5l);

        var result =  list.stream().peek(item ->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).map(item -> new Task(item,"Task: "+item,"status",List.of()))
                .peek(task -> System.err.println("Processing: "+ task.toString())).toList();
        long end = Calendar.getInstance().getTimeInMillis();
        long cost = end- start;
        System.out.println("Cost(bio): "+ cost);
        return result;
    }
}
