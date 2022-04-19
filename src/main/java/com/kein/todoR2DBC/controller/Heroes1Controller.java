package com.kein.todoR2DBC.controller;

import com.kein.todoR2DBC.domain.Heroes1;
import com.kein.todoR2DBC.service.Heroes1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class Heroes1Controller {
    @Autowired
    Heroes1Service heroes1Service;
    @GetMapping(value = "/heroes",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Heroes1> getHeroes(){
        return heroes1Service.getHeroes().delayElements(Duration.ofMillis(1000));
    }
}
