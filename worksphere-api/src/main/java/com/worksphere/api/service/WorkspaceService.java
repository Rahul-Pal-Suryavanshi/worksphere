package com.worksphere.api.service;


import com.worksphere.api.dto.WorkspaceRequest;
import com.worksphere.api.dto.WorkspaceResponse;
import com.worksphere.api.entity.User;
import com.worksphere.api.entity.Workspace;
import com.worksphere.api.repository.UserRepository;
import com.worksphere.api.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public WorkspaceResponse createWorkspace(
            WorkspaceRequest request,
            Authentication authentication
    ){

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        Workspace workspace = Workspace.builder()
                .name(request.getName())
                .owner(user)
                .build();

        Workspace savedWorkspace = workspaceRepository.save(workspace);



        return WorkspaceResponse.builder()
                .id(savedWorkspace.getId())
                .name(savedWorkspace.getName())
                .ownerEmail(savedWorkspace.getOwner().getEmail())
                .build();
    }

    public List<WorkspaceResponse> getMyWorkspaces(
            Authentication authentication
    ){

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        return workspaceRepository.findByOwner(user)
                .stream()
                .map(workspace -> WorkspaceResponse.builder()
                        .id(workspace.getId())
                        .name(workspace.getName())
                        .ownerEmail(
                                workspace.getOwner().getEmail()
                        ).build())
                .toList();
    }
}
