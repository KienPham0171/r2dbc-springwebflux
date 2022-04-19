package com.kein.todoR2DBC.repository.impl;

import com.kein.todoR2DBC.domain.Task;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Component
public abstract class TaskRepositoryImpl  {
    @Autowired
    ConnectionFactory factory;
    public Mono<Void> createTask(Task task){

      Flux.from(factory.create()).flatMap(conn ->{
            Statement statement = conn.createStatement("Insert into Task(name,status) values (:name,:status)")
                    .bind("name",task.getName())
                    .bind("status",task.getStatus()).returnGeneratedValues("id");
            Flux.from(statement.execute()).flatMap(result -> result.map((row,metadata) -> {
                Integer id = row.get(0, Integer.class);
                String descriptionFromAlias = row.get("task", String. class);
                String isCompleted = (row.get(2, Boolean.class) == true) ? "Yes" : "No";
                return String.format("ID: %s - Description: %s Completed: %s", id, descriptionFromAlias, isCompleted );

            })).subscribe(result-> System.err.println(result));
            return Flux.just("ok");
        });

        return null;
    }


}
