package org.congnguyen.taskorganiser.persistence.repositories;

import org.congnguyen.taskorganiser.persistence.models.ChildrenStatusStats;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildrenStatusStatsRepository extends Neo4jRepository<ChildrenStatusStats, String> {
//    @Query("MATCH (p:Task)<-[:memberOf]-(c:Task) WHERE p.code = $code RETURN c")
    @Query("match (n) <-[r:memberOf]-(p) WHERE n.code = $code return n.code as code, p.status as status, count(p) as count;")
    List<ChildrenStatusStats> findByTaskCode(@Param("code") String code);
}
