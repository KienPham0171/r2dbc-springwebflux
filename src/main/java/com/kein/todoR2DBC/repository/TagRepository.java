package com.kein.todoR2DBC.repository;

import com.kein.todoR2DBC.domain.Tag;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends ReactiveCrudRepository<Tag,Long> {
}
