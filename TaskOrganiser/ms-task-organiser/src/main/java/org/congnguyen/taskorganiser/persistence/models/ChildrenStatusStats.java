package org.congnguyen.taskorganiser.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

@EqualsAndHashCode(callSuper= false)
@Data
public class ChildrenStatusStats {
    @Id
    @Property("code")
    private String code;
    @Property("status")
    private String status;

    @Property("count")
    private int count;
}
