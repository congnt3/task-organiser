package org.congnguyen.taskorganiser.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskModel {
    private String code;

    private String name;

    private String description;

    private String status;

    private String parentCode;

    private List<String> dependsOnTasks;

    private List<ExternalLink> externalLinks;
}
