package com.kein.todoR2DBC.repository;

import com.kein.todoR2DBC.domain.Task;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TaskRepository extends ReactiveCrudRepository<Task,Long> {
    Mono<Void> deleteTaskById(long taskId);
    Flux<Task> findTasksByStatus(String status);
}
