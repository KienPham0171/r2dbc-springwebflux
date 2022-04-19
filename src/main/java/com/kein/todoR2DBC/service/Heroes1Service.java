package com.kein.todoR2DBC.service;

import com.kein.todoR2DBC.domain.Heroes1;
import com.kein.todoR2DBC.repository.Heroes1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class Heroes1Service {
    @Autowired
    Heroes1Repository heroes1Repo;

    public Flux<Heroes1> getHeroes(){
        return heroes1Repo.findAll();
    }
}
