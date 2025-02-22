package org.congnguyen.taskorganiser.persistence.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import org.congnguyen.taskorganiser.domain.models.IExternalLink;
import org.congnguyen.taskorganiser.domain.models.ITask;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("Task")
@EqualsAndHashCode(callSuper=true)
@Data
public class Task extends ModelBase  {
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

    @Relationship(value = "memberOf", direction = Relationship.Direction.OUTGOING, cascadeUpdates = false)
    private Task parent;

    @Relationship(value = "memberOf", direction = Relationship.Direction.INCOMING, cascadeUpdates = false)

    private List<Task> children;

    @Relationship(value = "dependsOn", direction = Relationship.Direction.OUTGOING, cascadeUpdates = false)
    private List<Task> dependsOn;
}
