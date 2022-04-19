package com.kein.todoR2DBC.service;

import com.kein.todoR2DBC.domain.Tag;
import com.kein.todoR2DBC.domain.Task;
import com.kein.todoR2DBC.mapper.TagTaskMapper;
import com.kein.todoR2DBC.repository.TagRepository;
import com.kein.todoR2DBC.repository.TagTaskRepository;
import com.kein.todoR2DBC.repository.TaskRepository;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TagTaskRepository tagTaskRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    TagTaskMapper tagTaskMapper;
    @Autowired
    private KafkaAdmin admin;
    @Autowired
    private KafkaTemplate<String,String> template;



    public  Mono<Task> createTask(Task task) {
       return  taskRepository.save(task)
                .flatMap(t -> tagTaskRepository.saveAll(tagTaskMapper.mapRealTaskToTagTask(t)).collectList()
                        .doOnSuccess(list -> sendNotificationToKafka())
                        .then(findTaskById(t.getId())));
    }
    public Mono<Task> findTaskById(long id){

        return taskRepository.findById(id)
                .flatMap(task -> Mono.just(task).zipWith(tagTaskRepository.findAllByTaskId(id)
                        .flatMap(tagtask -> tagRepository.findById(tagtask.getTagId()))
                        .collectList()).map(
                                result ->{
                                    Task t = result.getT1();
                                    t.setTags(result.getT2());
                                    return t;
                                }
                ));
    }
    public Mono<Task> updateTask(Task task){
        return tagTaskRepository.deleteAllByTaskId(task.getId()).then(
                createTask(task)
        );
    }
    public Mono<Integer> delTaskById(long id){

        return taskRepository.deleteTaskById(id)
                .then(tagTaskRepository.deleteAllByTaskId(id))
                .doOnSuccess(i->sendNotificationToKafka());
    }
    public Flux<Task> findTasksByStatus(String status){
        return taskRepository.findTasksByStatus(status)
                .flatMap(this::loadRelations);
    }

    public Flux<Task> getTasks(){
        Flux<Task> tasks =taskRepository.findAll()
                .flatMap(task -> loadRelations(task));
        return tasks;
    }
    public void sendNotificationToKafka(){
        AdminClient client  = AdminClient.create(admin.getConfigurationProperties());
        String topic = "notifications16";
        NewTopic to = new NewTopic(topic,1, (short) 1);
        client.createTopics(Arrays.asList(to));

        ProducerRecord<String,String> record = new ProducerRecord<>(topic,"refresh");
        ListenableFuture<SendResult<String,String>> future =  template.send(record);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("success");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("failure");
            }
        });
    }

    private Mono<Task> loadRelations(final Task task) {


        Mono<List<Tag>> monoListTask =tagTaskRepository.findAllByTaskId(task.getId())
                .flatMap(tagtask -> tagRepository.findById(tagtask.getTagId()))
                .collectList();
        Flux<Tag> abc =tagTaskRepository.findAllByTaskId(task.getId())
                .flatMap(tagtask -> tagRepository.findById(tagtask.getTagId()));
        Mono<Task> mono1 = Mono.just(task)
                .zipWith(monoListTask)
                .map(result -> {
                    Task sm = result.getT1();
                    List<Tag> a = result.getT2();
                    sm.setTags(a);
                    return sm;

                });

        return mono1;
    }

    public Flux<Task> findTasksByTagId(long id) {
        return tagTaskRepository.findAllByTagId(id)
                .flatMap(tagtask ->{
                    return this.findTaskById(tagtask.getTaskId());
                });
    }
}
