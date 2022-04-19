package com.kein.todoR2DBC.repository;

import com.kein.todoR2DBC.domain.Heroes1;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Heroes1Repository extends ReactiveCrudRepository<Heroes1,Long> {
}
