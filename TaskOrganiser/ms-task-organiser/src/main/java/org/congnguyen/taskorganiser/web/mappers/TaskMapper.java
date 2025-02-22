package org.congnguyen.taskorganiser.web.mappers;

import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.persistence.repositories.TaskRepository;
import org.congnguyen.taskorganiser.persistence.exceptions.RecordNotFoundException;
import org.congnguyen.taskorganiser.web.models.CreateTaskRequest;
import org.congnguyen.taskorganiser.web.models.TaskModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    @Autowired
    TaskRepository taskRepository;

    @Mapping(target = "externalLinks", ignore = true)
    @Mapping(target = "parent", source = "parentCode", qualifiedByName = "codeToTask")
    @Mapping(target = "dependsOn", source = "dependsOnTasks", qualifiedByName = "codesToTasks")
    public abstract Task createTaskRequestToTask(CreateTaskRequest request);

    @Mapping(target = "parentCode", source = "parent.code")
    public abstract TaskModel taskToTaskModel(Task task);

    @Named("codeToTask")
    protected Task stringToTask(String code) throws RecordNotFoundException {
        return taskRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Task with code %s not found", code)));
    }

    @Named("codesToTasks")
    protected List<Task> codesToTasks(Collection<String> codes) throws RecordNotFoundException {
        var tasks  = new ArrayList<Task>();
        for (String code : codes) {
            var task = taskRepository.findByCode(code);
            if (!task.isPresent()) {
                throw new RecordNotFoundException(String.format("Task with code %s not found", code));
            }

            tasks.add(task.get());
        }

        return tasks;
    }
}
