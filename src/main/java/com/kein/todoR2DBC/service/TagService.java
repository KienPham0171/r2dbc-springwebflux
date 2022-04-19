package com.kein.todoR2DBC.service;

import com.kein.todoR2DBC.domain.Tag;
import com.kein.todoR2DBC.domain.Task;
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
public class TagService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TagTaskRepository tagTaskRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    private KafkaAdmin admin;
    @Autowired
    private KafkaTemplate<String,String> template;

    public Flux<Tag> getTags(){
        return tagRepository.findAll()
                .flatMap(tag -> Mono.just(tag)
                        .zipWith(tagTaskRepository.findAllByTagId(tag.getId())
                                .flatMap(tagtask-> taskRepository.findById(tagtask.getTaskId()))
                                .collectList())
                        .map(result -> {
                            Tag sm =result.getT1();
                            List<Task> tasks =result.getT2();
                            sm.setTasks(tasks);
                            return sm;
                        })
                );
    }
    public Mono<Tag> createTag(Tag tag){
        return tagRepository.save(tag).doOnSuccess(t->sendNotificationToKafka());
    }

    public Mono<Void> deleteTagById(Long id) {
        return tagRepository.deleteById(id)
                .then(tagTaskRepository.deleteAllByTagId(id))
                .doOnSuccess(sm ->sendNotificationToKafka());
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
}
