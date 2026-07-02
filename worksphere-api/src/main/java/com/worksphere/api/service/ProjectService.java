package com.worksphere.api.service;

import com.worksphere.api.dto.ProjectRequest;
import com.worksphere.api.dto.ProjectResponse;
import com.worksphere.api.entity.Project;
import com.worksphere.api.entity.User;
import com.worksphere.api.entity.Workspace;
import com.worksphere.api.repository.ProjectRepository;
import com.worksphere.api.repository.UserRepository;
import com.worksphere.api.repository.WorkspaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public ProjectResponse createProject(
            UUID workspaceId,
            ProjectRequest request,
            Authentication authentication
    ){
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        Workspace workspace =workspaceRepository
                .findById(workspaceId)
                .orElseThrow(()->
                        new RuntimeException("Workspace not found"));

        if(!workspace.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("You are not allowed to access this workspace");
        }

        Project project =Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .workspace(workspace)
                .build();

        Project savedProject = projectRepository.save(project);

        return ProjectResponse.builder()
                .id(savedProject.getId())
                .name(savedProject.getName())
                .description(savedProject.getDescription())
                .workspaceName(
                        savedProject.getWorkspace().getName()
                )
                .build();
    }

    public List<ProjectResponse> getProjectsByWorkspace(
            UUID workspaceId,
            Authentication authentication
    ){
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        Workspace workspace = workspaceRepository
                .findById(workspaceId)
                .orElseThrow(()->
                        new RuntimeException("Workspace not found"));

        if(!workspace.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("You are not allowed to access this workspace");
        }

        return projectRepository.findByWorkspace(workspace)
                .stream()
                .map(project -> ProjectResponse.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .description(project.getDescription())
                        .workspaceName(workspace.getName())
                        .build())
                .toList();
    }
}
