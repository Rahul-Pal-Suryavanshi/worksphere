package com.worksphere.api.service;


import com.worksphere.api.dto.TaskRequest;
import com.worksphere.api.dto.TaskResponse;
import com.worksphere.api.entity.Project;
import com.worksphere.api.entity.Task;
import com.worksphere.api.entity.User;
import com.worksphere.api.entity.Workspace;
import com.worksphere.api.enums.TaskStatus;
import com.worksphere.api.repository.ProjectRepository;
import com.worksphere.api.repository.TaskRepository;
import com.worksphere.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(
            UUID projectId,
            TaskRequest request,
            Authentication authentication
    ){

        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User Not Found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->
                        new RuntimeException("Project Not Found"));

        Workspace workspace = project.getWorkspace();

        if(!workspace.getOwner().getId()
                .equals(loggedInUser.getId())){
            throw new RuntimeException("You are not allowed to access this project");
        }

        User assignedUser = userRepository
                .findByEmail(request.getAssignedUserEmail())
                .orElseThrow(()->
                        new RuntimeException("Assigned User Not Found"));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(TaskStatus.TODO)
                .createdAt(LocalDateTime.now())
                .project(project)
                .assignedUser(assignedUser)
                .build();

        Task savedTask = taskRepository.save(task);

        return TaskResponse.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .status(savedTask.getStatus())
                .priority(savedTask.getPriority())
                .assignedUserEmail(
                        savedTask.getAssignedUser().getEmail()
                )
                .projectName(savedTask.getProject().getName())
                .createdAt(savedTask.getCreatedAt())
                .build();
    }

    public List<TaskResponse> getTaskByProject(
            UUID projectId,
            Authentication authentication
    ){
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User Not Found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->
                        new RuntimeException("Project Not Found"));

        Workspace workspace = project.getWorkspace();

        if(!workspace.getOwner().getId()
                .equals(loggedInUser.getId())){
            throw new RuntimeException("You are not allowed to access this prokect");
        }

        return taskRepository.findByProject(project)
                .stream()
                .map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .status(task.getStatus())
                        .priority(task.getPriority())
                        .assignedUserEmail(task.getAssignedUser().getEmail())
                        .projectName(project.getName())
                        .createdAt(task.getCreatedAt())
                        .build())
                .toList();
    }
}
