package org.congnguyen.taskorganiser.web.mappers;

import org.congnguyen.taskorganiser.persistence.repositories.TaskRepository;
import org.congnguyen.taskorganiser.web.exceptions.RecordNotFoundException;
import org.congnguyen.taskorganiser.web.models.CreateTaskRequest;
import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.web.models.TaskModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    @Autowired
    TaskRepository taskRepository;

    @Mapping(target = "externalLinks", ignore = true)
    @Mapping(target = "parent", source = "parentCode", qualifiedByName = "codeToTask")
    @Mapping(target = "dependsOn", ignore = true)
    public abstract Task createTaskRequestToTask(CreateTaskRequest request);

    public abstract TaskModel taskToTaskModel(Task task);

    @Named("codeToTask")
    protected Task stringToTask(String code) throws RecordNotFoundException {
        return taskRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Task with code %s not found", code)));
    }
}
