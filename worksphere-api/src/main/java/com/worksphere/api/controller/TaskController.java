package com.worksphere.api.controller;


import com.worksphere.api.dto.TaskRequest;
import com.worksphere.api.dto.TaskResponse;
import com.worksphere.api.dto.UpdateTaskRequest;
import com.worksphere.api.dto.UpdateTaskStatusRequest;
import com.worksphere.api.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("{projectId}")
    public TaskResponse createTask(
            @PathVariable UUID projectId,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication
            ){
        return taskService.createTask(
                projectId,
                request,
                authentication
        );
    }

    @GetMapping("{projectId}")
    public List<TaskResponse> getTasks(
            @PathVariable UUID projectId,
            Authentication authentication
    ){

        return taskService.getTaskByProject(
                projectId, authentication);
    }

    @PatchMapping("/{taskId}/status")
    public TaskResponse updateTaskStatus(
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            Authentication authentication
            ){
        return taskService.updateTaskStatus(
                taskId,
                request,
                authentication
        );
    }

    @PutMapping("/{taskId}")
    public TaskResponse updateTask(
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskRequest request,
            Authentication authentication
            ){
        return taskService.updateTask(taskId, request, authentication);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID taskId,
            Authentication authentication
    ){

        taskService.deleteTask(taskId, authentication);

        return ResponseEntity.noContent().build();
    }
}
