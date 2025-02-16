package org.congnguyen.taskorganiser.web.controllers;

import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.services.TaskService;
import org.congnguyen.taskorganiser.web.models.CreateTaskRequest;
import org.congnguyen.taskorganiser.web.models.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Validated @RequestBody CreateTaskRequest request) {
        try {
            Task createdTask = taskService.createTask(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Task> getTaskByCode(@PathVariable("code") String code) {
        try {
            Task task = taskService.getTaskByCode(code);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
