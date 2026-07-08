package com.worksphere.api.controller;


import com.worksphere.api.dto.TaskRequest;
import com.worksphere.api.dto.TaskResponse;
import com.worksphere.api.dto.UpdateTaskStatusRequest;
import com.worksphere.api.service.TaskService;
import lombok.RequiredArgsConstructor;
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
            @RequestBody TaskRequest request,
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
            @RequestBody UpdateTaskStatusRequest request,
            Authentication authentication
            ){
        return taskService.updateTaskStatus(
                taskId,
                request,
                authentication
        );
    }
}
