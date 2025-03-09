package org.congnguyen.taskorganiser.web.mappers;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    @Autowired
    TaskRepository taskRepository;

    @Mapping(target = "externalLinks", ignore = true)
    @Mapping(target = "parent", source = "parentCode", qualifiedByName = "codeToTask")
    @Mapping(target = "dependsOn", source = "dependsOnTasks", qualifiedByName = "codesToTasks")
    public abstract Task createTaskRequestToTask(CreateTaskRequest request);

    /**
     * Return the mapped TaskModel object with only first level dependencies
     * @param task
     * @return
     */
    @Mapping(target = "parentCode", source = "parent.code")
    @Mapping(target = "dependsOn", source = "dependsOn", qualifiedByName = "mapDependsOn")
    public abstract TaskModel taskToTaskModel(Task task);

    @Named("codeToTask")
    protected Task stringToTask(String code) throws RecordNotFoundException {
        if (!StringUtils.isNotEmpty(code)) {
            return null;
        }

        return taskRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Task with code %s not found", code)));
    }

    @Named("codesToTasks")
    protected List<Task> codesToTasks(Collection<String> codes) throws RecordNotFoundException {
        if (codes == null) {
            return null;
        }
        var tasks = new ArrayList<Task>();
        for (String code : codes) {
            var task = taskRepository.findByCode(code);
            if (!task.isPresent()) {
                throw new RecordNotFoundException(String.format("Task with code %s not found", code));
            }

            tasks.add(task.get());
        }

        return tasks;
    }

    @Mapping(target = "dependsOn", ignore = true)
    public abstract Task copyTaskEntityWithoutDependencies(Task input);

    @Named("mapDependsOn")
    protected List<TaskModel> mapDependsOn(List<Task> input) {
        if (input == null){
            return null;
        }

        var result = input.stream()
                .map(this::copyTaskEntityWithoutDependencies)
                .map(this::taskToTaskModel).toList();
        return result;
    }
}
