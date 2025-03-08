package org.congnguyen.taskorganiser.web.controllers;

import org.congnguyen.taskorganiser.persistence.exceptions.DuplicatedRecordException;
import org.congnguyen.taskorganiser.persistence.models.Task;
import org.congnguyen.taskorganiser.persistence.repositories.TaskRepository;
import org.congnguyen.taskorganiser.services.TaskService;
import org.congnguyen.taskorganiser.persistence.exceptions.RecordNotFoundException;
import org.congnguyen.taskorganiser.web.mappers.TaskMapperImpl;
import org.congnguyen.taskorganiser.web.models.CreateTaskRequest;
import org.congnguyen.taskorganiser.web.models.ErrorResponse;
import org.congnguyen.taskorganiser.web.models.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapperImpl taskMapperImpl;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskService taskService, TaskMapperImpl taskMapperImpl, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskMapperImpl = taskMapperImpl;
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Validated @RequestBody CreateTaskRequest request) {
        try {
            var task = taskMapperImpl.createTaskRequestToTask(request);
            var createdTask = taskService.createTask(task);
            var taskModel = taskMapperImpl.taskToTaskModel(createdTask);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskModel);
        } catch (DuplicatedRecordException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/code/{code}")
    public ResponseEntity<?> updateTask(
            @PathVariable("code") String code,
            @Validated @RequestBody CreateTaskRequest request) {
        try {
            var task = taskMapperImpl.createTaskRequestToTask(request);
            var updatedTask = taskService.updateTask(code, task);
            var taskModel = taskMapperImpl.taskToTaskModel(updatedTask);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskModel);
        } catch (RecordNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/code/{code}/status")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable("code") String code,
            @RequestBody CreateTaskRequest request) {
        try {
            var task = taskService.getTaskByCode(code);
            task.setStatus(request.getStatus());

            var updatedTask = taskService.updateTask(code, task);
            var taskModel = taskMapperImpl.taskToTaskModel(updatedTask);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskModel);
        } catch (RecordNotFoundException e) {
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
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Not working
    @GetMapping("/parent/{parent_code}")
    public ResponseEntity<List<TaskModel>> getTaskByParentCode(@PathVariable("parent_code") String code) {
        List<Task> result = null;
        if ("root".equalsIgnoreCase(code)) {
            result = taskRepository.findTopLevelTasks();
        } else {
            var tasks = taskService.findTaskByCode(code);
            if (tasks.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            result = taskRepository.findChildrenByTaskCode(tasks.get().getCode());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result.stream()
                        .map(taskMapperImpl::taskToTaskModel).toList());
    }

    @DeleteMapping("/code/{code}")
    public ResponseEntity<TaskModel> deleteTaskByCode(@PathVariable("code") String code) {
        try {
            Task task = taskService.getTaskByCode(code);
            taskRepository.deleteById(code);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(taskMapperImpl.taskToTaskModel(task));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<TaskModel>> getTopLevelTasks() {
        return ResponseEntity.ok(
                taskRepository.findTopLevelTasks().stream()
                        .map(taskMapperImpl::taskToTaskModel).toList());
    }
}
