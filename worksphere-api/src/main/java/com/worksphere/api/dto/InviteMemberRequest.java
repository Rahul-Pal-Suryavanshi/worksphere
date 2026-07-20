package com.worksphere.api.dto;

import com.worksphere.api.enums.WorkspaceRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteMemberRequest {
    private String email;
    private WorkspaceRole role;
}
