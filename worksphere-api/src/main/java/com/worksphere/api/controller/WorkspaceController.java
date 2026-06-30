package com.worksphere.api.controller;

import com.worksphere.api.dto.WorkspaceRequest;
import com.worksphere.api.dto.WorkspaceResponse;
import com.worksphere.api.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public WorkspaceResponse createWorkspace(
            @RequestBody WorkspaceRequest request,
            Authentication authentication
            ){
        return workspaceService.createWorkspace(
                request, authentication);
    }

    @GetMapping
    public List<WorkspaceResponse> getMyWorkspaces(
            Authentication authentication
    ){
        return workspaceService.getMyWorkspaces(authentication);
    }
}
