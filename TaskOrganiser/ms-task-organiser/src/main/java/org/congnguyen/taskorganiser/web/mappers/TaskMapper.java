package org.congnguyen.taskorganiser.web.mappers;

import org.congnguyen.taskorganiser.web.models.CreateTaskRequest;
import org.congnguyen.taskorganiser.persistence.models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "externalLinks", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "dependsOn", ignore = true)
    Task createTaskRequestToTask(CreateTaskRequest request);

    // Add more mapping methods as needed
}
