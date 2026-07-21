package com.worksphere.api.service;


import com.worksphere.api.dto.*;
import com.worksphere.api.entity.User;
import com.worksphere.api.entity.Workspace;
import com.worksphere.api.entity.WorkspaceMember;
import com.worksphere.api.enums.WorkspaceRole;
import com.worksphere.api.exception.BadRequestException;
import com.worksphere.api.exception.ResourceNotFoundException;
import com.worksphere.api.exception.UnauthorizedException;
import com.worksphere.api.repository.UserRepository;
import com.worksphere.api.repository.WorkspaceMemberRepository;
import com.worksphere.api.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;


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

        WorkspaceMember ownerMember = WorkspaceMember.builder()
                .workspace(savedWorkspace)
                .user(user)
                .role(WorkspaceRole.OWNER)
                .build();

        workspaceMemberRepository.save(ownerMember);

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

    public WorkspaceMemberResponse inviteMember(
            UUID workspaceId,
            InviteMemberRequest request,
            Authentication authentication
    ){
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Workspace not found"));

        //Only owner can invite members
        WorkspaceMember ownerMember = workspaceMemberRepository
                .findByWorkspaceAndUser(workspace, loggedInUser)
                .orElseThrow(()->
                        new UnauthorizedException("You are not a workspace member"));

        if(ownerMember.getRole() != WorkspaceRole.OWNER){
            throw new UnauthorizedException("Only owner can invite members");
        }

        User invitedUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->
                        new RuntimeException("Invited User Not Found"));

        if(workspaceMemberRepository.existsByWorkspaceAndUser(workspace, invitedUser)){
            throw new BadRequestException("User is already a workspace member");
        }

        WorkspaceMember member = WorkspaceMember.builder()
                .workspace(workspace)
                .user(invitedUser)
                .role(request.getRole())
                .build();

        workspaceMemberRepository.save(member);

        return WorkspaceMemberResponse.builder()
                .workspaceName(workspace.getName())
                .memberId(invitedUser.getId())
                .memberName(invitedUser.getName())
                .memberEmail(invitedUser.getEmail())
                .role(member.getRole())
                .build();
    }

    public List<WorkspaceMemberResponse> getWorkspaceMembers(
            UUID workspaceId,
            Authentication authentication
    ){
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Workspace not found"));

        workspaceMemberRepository.findByWorkspaceAndUser(workspace, loggedInUser)
                .orElseThrow(()->
                        new RuntimeException("You are not a member of this workspace"));

        return workspaceMemberRepository.findByWorkspace(workspace)
                .stream()
                .map(member -> WorkspaceMemberResponse.builder()
                        .workspaceName(workspace.getName())
                        .memberId(member.getId())
                        .memberName(member.getUser().getName())
                        .memberEmail(member.getUser().getEmail())
                        .role(member.getRole())
                        .build())
                .toList();
    }

    public WorkspaceMemberResponse updateMemberRole(
            UUID workspaceId,
            UUID memberId,
            UpdateMemberRoleRequest request,
            Authentication authentication
    ){
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Workspace not found"));

        WorkspaceMember ownerMember = workspaceMemberRepository.findByWorkspaceAndUser(workspace, loggedInUser)
                .orElseThrow(()->
                        new BadRequestException("You are not a workspace member"));

        if(ownerMember.getRole()!= WorkspaceRole.OWNER){
            throw  new UnauthorizedException("Only owner can change roles");
        }

        WorkspaceMember member = workspaceMemberRepository
                .findById(memberId)
                .orElseThrow(()-> new RuntimeException("Member not found"));

        if(!member.getWorkspace().getId().equals(workspaceId)){
            throw new ResourceNotFoundException("Member does not belong this workspace");
        }

        if(member.getRole() == WorkspaceRole.OWNER){
            throw new BadRequestException("Owner role cannot be changed");
        }

        member.setRole(request.getRole());

        WorkspaceMember updated = workspaceMemberRepository.save(member);

        return WorkspaceMemberResponse.builder()
                .workspaceName(workspace.getName())
                .memberId(updated.getId())
                .memberName(updated.getUser().getName())
                .memberEmail(updated.getUser().getEmail())
                .role(updated.getRole())
                .build();
    }

    public void removeMember(
            UUID workspaceId,
            UUID memberId,
            Authentication authentication
    ){
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new ResourceNotFoundException("User not found"));

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Workspace not found"));

        WorkspaceMember ownerMember = workspaceMemberRepository.findByWorkspaceAndUser(workspace, loggedInUser)
                .orElseThrow(()->
                        new RuntimeException("You are not a workspace member"));

        if(ownerMember.getRole()!= WorkspaceRole.OWNER){
            throw  new UnauthorizedException("Only owner can remove members");
        }

        WorkspaceMember member = workspaceMemberRepository
                .findByWorkspaceAndId(workspace, memberId)
                .orElseThrow(()->
                new BadRequestException("Member not found"));

        if(!member.getWorkspace().getId().equals(workspaceId)){
            throw new BadRequestException("Member does not belongs to this workspace");
        }

        if(member.getRole()== WorkspaceRole.OWNER){
            throw new BadRequestException("Owner cannot be removed");
        }

        workspaceMemberRepository.delete(member);
    }



}
