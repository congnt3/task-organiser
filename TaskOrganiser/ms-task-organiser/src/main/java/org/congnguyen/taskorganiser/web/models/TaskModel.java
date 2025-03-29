package org.congnguyen.taskorganiser.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskModel {
    private String code;

    private String name;

    private String type;

    private String description;

    private String status;

    private String parentCode;

    private LocalDateTime dueDate;

    private int estimatedEffortDays;

    private LocalDateTime plannedStartDate;

    private LocalDateTime plannedCompletionDate;

    private List<TaskModel> children;

    private List<TaskModel> dependsOn;

    private List<ExternalLink> externalLinks;
}
