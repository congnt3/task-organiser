package org.congnguyen.taskorganiser.services;

import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.persistence.repositories.TaskRepository;
import org.congnguyen.taskorganiser.web.mappers.TaskMapper;
import org.congnguyen.taskorganiser.web.models.CreateTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Task getTaskByCode(String code) {
        return taskRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Task not found with code: " + code));
    }

    public Task createTask(CreateTaskRequest request) {
        if (taskRepository.findByCode(request.getCode()).isPresent()) {
            throw new RuntimeException("Task with code " + request.getCode() + " already exists");
        }

        Task newTask = taskMapper.createTaskRequestToTask(request);
        return taskRepository.save(newTask);
    }
}
