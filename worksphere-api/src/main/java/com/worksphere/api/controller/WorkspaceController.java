package com.worksphere.api.controller;

import com.worksphere.api.dto.*;
import com.worksphere.api.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkspaceResponse>> createWorkspace(
            @Valid @RequestBody WorkspaceRequest request,
            Authentication authentication
            ){
        WorkspaceResponse response = workspaceService.createWorkspace(
                request, authentication);

        return ResponseEntity.ok(
                ApiResponse.<WorkspaceResponse>builder()
                        .success(true)
                        .message("Workspace created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public List<WorkspaceResponse> getMyWorkspaces(
            Authentication authentication
    ){
        return workspaceService.getMyWorkspaces(authentication);
    }

    @PostMapping("/{workspaceId}/members")
    public WorkspaceMemberResponse inviteMember(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody InviteMemberRequest request,
            Authentication authentication
            ){
        return workspaceService.inviteMember(
                workspaceId,
                request,
                authentication
        );
    }

    @GetMapping("/{workspaceId}/members")
    public List<WorkspaceMemberResponse> getWorkspaceMembers(
            @PathVariable UUID workspaceId,
            Authentication authentication
    ){
        return workspaceService.getWorkspaceMembers(workspaceId, authentication);
    }

    @PatchMapping("/{workspaceId}/members/{memberId}/role")
    public WorkspaceMemberResponse updateMemberRole(
            @PathVariable UUID workspaceId,
            @PathVariable UUID memberId,
            @Valid @RequestBody UpdateMemberRoleRequest request,
            Authentication authentication
            ){
        return workspaceService.updateMemberRole(
                workspaceId, memberId, request, authentication);
    }

    @DeleteMapping("/{workspaceId}/members/{memberId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID workspaceId,
            @PathVariable UUID memberId,
            Authentication authentication
    ){
        workspaceService.removeMember(
                workspaceId, memberId, authentication);
        return ResponseEntity.noContent().build();
    }
}
