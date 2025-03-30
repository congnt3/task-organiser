package org.congnguyen.taskorganiser.services;

import lombok.extern.log4j.Log4j2;
import org.congnguyen.taskorganiser.persistence.exceptions.DuplicatedRecordException;
import org.congnguyen.taskorganiser.persistence.exceptions.RecordNotFoundException;
import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.persistence.repositories.ChildrenStatusStatsRepository;
import org.congnguyen.taskorganiser.persistence.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class TaskService {

    private final TaskRepository taskRepository;

    private final ChildrenStatusStatsRepository childrenStatusStatsRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ChildrenStatusStatsRepository childrenStatusStatsRepository) {
        this.taskRepository = taskRepository;
        this.childrenStatusStatsRepository = childrenStatusStatsRepository;
    }

    public Task getTaskByCode(String code) throws RecordNotFoundException {
        return getTaskByCode(code, false);
    }

    public Task getTaskByCode(String code, boolean withDependencies) throws RecordNotFoundException {
        var task = taskRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Task with code %s not exists.", code)));
        if (withDependencies) {
            task.setDependsOn(taskRepository.findDependenciesByTaskCode(code));
        }

        var childStats = childrenStatusStatsRepository.findByTaskCode(code);
        task.setChildrenStats(childStats);

        return task;
    }

    public Optional<Task> findTaskByCode(String code) {
        return taskRepository.findByCode(code);
    }

    public Task createTask(Task request) throws DuplicatedRecordException {
        if (taskRepository.findByCode(request.getCode()).isPresent()) {
            throw new DuplicatedRecordException(String.format("Task with code %s already exists.", request.getCode()));
        }

        return taskRepository.save(request);
    }

    public Task updateTask(String code, Task request) throws RecordNotFoundException {
        var task = taskRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Task with code %s not exists.", code)));

        if (request.getDependsOn() != null) {
            task.getDependsOn().addAll(request.getDependsOn());
        }

        request.setCode(code);
        return taskRepository.save(request);
    }

    public Task addDependencies(String code, List<String> deps) throws RecordNotFoundException {
        var task = this.getTaskByCode(code);
        for (String d : deps) {
            task.getDependsOn().add(this.getTaskByCode(d));
        }

        return taskRepository.save(task);
    }

    public boolean removeDependencies(String code, String dependsOn) {
        taskRepository.removeDependency(code, dependsOn);
        return true;
    }

    public List<Task> findTaskByParentCode(String code) {
        var result = taskRepository.findChildrenByTaskCode(code);
        result.forEach(r -> r.setChildrenStats(childrenStatusStatsRepository.findByTaskCode(r.getCode())));

        return result;
    }

    public List<Task> findTopLevelTasks() {
        var result =  taskRepository.findTopLevelTasks();
        result.forEach(r -> r.setChildrenStats(childrenStatusStatsRepository.findByTaskCode(r.getCode())));

        return result;
    }

    public List<Task> queryChildrenThatContainsDescendantWithStatus(String rootCode, List<String> statuses) {
        var result =  taskRepository.queryChildrenThatContainsDescendantWithStatus(rootCode, statuses);
        result.forEach(r -> r.setChildrenStats(childrenStatusStatsRepository.findByTaskCode(r.getCode())));

        return result;
    }

    public List<Task> queryTopLevelTasksThatContainsDescendantWithStatus(List<String> statuses) {
        var result =  taskRepository.queryTopLevelTasksThatContainsDescendantWithStatus(statuses);
        result.forEach(r -> r.setChildrenStats(childrenStatusStatsRepository.findByTaskCode(r.getCode())));

        return result;
    }
}
