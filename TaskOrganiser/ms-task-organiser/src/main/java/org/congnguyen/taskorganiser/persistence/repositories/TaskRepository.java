package org.congnguyen.taskorganiser.persistence.repositories;

import org.congnguyen.taskorganiser.persistence.models.Task;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends Neo4jRepository<Task, String> {
    Optional<Task> findByCode(String code);

    @Query("MATCH (p:Task)<-[:memberOf]-(c:Task) WHERE p.code = $code RETURN c")
    List<Task> findChildrenByTaskCode(@Param("code") String code);

    @Query("MATCH (t:Task)-[:DEPENDS_ON]->(d:Task) WHERE t.code = $code RETURN d")
    List<Task> findDependenciesByTaskCode(@Param("code") String code);

    @Query("MATCH (c:Task) WHERE NOT ()<-[:memberOf]-(c) RETURN c")
    List<Task> findTopLevelTasks();
}
