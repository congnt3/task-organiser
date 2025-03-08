package org.congnguyen.taskorganiser.web.models;

import lombok.Data;
import org.congnguyen.taskorganiser.domain.models.IExternalLink;

import java.util.List;

@Data
public class CreateTaskRequest {
//    @NotBlank(message = "Code is required")
    private String code;

//    @NotBlank(message = "Name is required")
    private String name;

    private String description;

//    @NotBlank(message = "Status is required")
    private String status;

    private String parentCode;

    private List<String> dependsOnTasks;

    private List<IExternalLink> externalLinks;
}
