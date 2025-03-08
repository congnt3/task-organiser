package org.congnguyen.taskorganiser.services;

import lombok.extern.log4j.Log4j2;
import org.congnguyen.taskorganiser.persistence.exceptions.DuplicatedRecordException;
import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.persistence.repositories.TaskRepository;
import org.congnguyen.taskorganiser.persistence.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTaskByCode(String code) throws RecordNotFoundException {
        return taskRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Task with code %s not exists.", code)));
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
                .orElseThrow(() -> new RecordNotFoundException(String.format("Task with code %s not exists.", request.getCode())));

        if (request.getDependsOn() != null) {
            task.getDependsOn().addAll(request.getDependsOn());
        }

        request.setCode(code);
        return taskRepository.save(request);
    }
}
