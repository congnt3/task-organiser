package org.congnguyen.taskorganiser.persistence.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.congnguyen.taskorganiser.domain.models.IExternalLink;
import org.congnguyen.taskorganiser.domain.models.ITask;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("Task")
@Builder
@EqualsAndHashCode(callSuper=true)
@Data
public class Task extends ModelBase implements ITask {
    @Property("code")
    @Id
    private String code;

    @Property("name")
    private String name;

    @Property("description")
    private String description;

    @Property("status")
    private String status;

    @Relationship(value = "belongsTo", direction = Relationship.Direction.INCOMING)
    private List<IExternalLink> externalLinks;

    @Relationship(value = "memberOf", direction = Relationship.Direction.OUTGOING)
    private Task parent;

    @Relationship(value = "dependsOn", direction = Relationship.Direction.OUTGOING)
    private List<Task> dependsOn;
}
