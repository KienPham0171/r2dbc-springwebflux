package com.kein.todoR2DBC.repository;

import com.kein.todoR2DBC.domain.TagTask;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TagTaskRepository extends ReactiveCrudRepository<TagTask,Long> {
    Flux<TagTask> findAllByTagId(long tagId);
    Flux<TagTask> findAllByTaskId(long taskId);
    Mono<Integer> deleteAllByTaskId(long taskId);
    Mono<Void> deleteAllByTagId(long tagId);
}
