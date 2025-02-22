package org.congnguyen.taskorganiser.web.controllers;

import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.services.TaskService;
import org.congnguyen.taskorganiser.web.mappers.TaskMapperImpl;
import org.congnguyen.taskorganiser.web.models.CreateTaskRequest;
import org.congnguyen.taskorganiser.web.models.ErrorResponse;
import org.congnguyen.taskorganiser.web.models.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapperImpl taskMapperImpl;

    @Autowired
    public TaskController(TaskService taskService, TaskMapperImpl taskMapperImpl) {
        this.taskService = taskService;
        this.taskMapperImpl = taskMapperImpl;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Validated @RequestBody CreateTaskRequest request) {
        try {
            Task createdTask = taskService.createTask(request);
            var taskModel = taskMapperImpl.taskToTaskModel(createdTask);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskModel);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<TaskModel> getTaskByCode(@PathVariable("code") String code) {
        try {
            Task task = taskService.getTaskByCode(code);
            return ResponseEntity.ok(taskMapperImpl.taskToTaskModel(task));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
