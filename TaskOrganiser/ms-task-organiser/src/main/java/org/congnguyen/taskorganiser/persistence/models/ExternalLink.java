package org.congnguyen.taskorganiser.persistence.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.congnguyen.taskorganiser.domain.models.IExternalLink;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Class")
@Builder
@AllArgsConstructor
@Data
public class ExternalLink implements IExternalLink {
    @Property("code")
    @Id
    private String code;

    @Property("name")
    private String name;

    @Property("description")
    private String description;

    @Property("url")
    private String url;

    @Relationship(value = "belongsTo", direction = Relationship.Direction.INCOMING)
    private Task belongsTo;
}
