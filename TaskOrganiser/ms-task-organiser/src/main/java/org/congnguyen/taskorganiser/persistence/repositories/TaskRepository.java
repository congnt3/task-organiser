package org.congnguyen.taskorganiser.persistence.repositories;

import org.congnguyen.taskorganiser.persistence.models.Task;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends Neo4jRepository<Task, String> {
    Optional<Task> findByCode(String code);
}
