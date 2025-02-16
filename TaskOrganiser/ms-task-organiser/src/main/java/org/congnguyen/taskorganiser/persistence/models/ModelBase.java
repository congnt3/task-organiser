package org.congnguyen.taskorganiser.persistence.models;

import lombok.Data;
import org.congnguyen.taskorganiser.domain.models.IModelBase;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDateTime;

@Data
public class ModelBase implements IModelBase {
    @CreatedDate
    @Property("created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Property("last_modified_date")
    private LocalDateTime modifiedDate;
}
