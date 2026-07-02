package com.worksphere.api.controller;


import com.worksphere.api.dto.ProjectRequest;
import com.worksphere.api.dto.ProjectResponse;
import com.worksphere.api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/{workspaceId}")
    public ProjectResponse createProject(
            @PathVariable UUID workspaceId,
            @RequestBody ProjectRequest request,
            Authentication authentication
            ){

        return projectService.createProject(
                workspaceId, request, authentication);
    }

    @GetMapping("/{workspaceId}")
    public List<ProjectResponse> getProjects(
            @PathVariable UUID workspaceId,
            Authentication authentication
    ){

        return projectService.getProjectsByWorkspace(
                workspaceId, authentication);
    }
}
