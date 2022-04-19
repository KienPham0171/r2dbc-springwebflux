package com.kein.todoR2DBC.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyMessageListener {
    @Autowired
    SimpMessagingTemplate sender;
    @Autowired
    ConsumerFactory<String,String> consumerFactory;
    @KafkaListener(topics = "notifications16",id = "myListener")
    public void listener(String notification) throws InterruptedException {
        String destination = "/topic/allUsers";
        sender.convertAndSend(destination,notification);
    }
}
