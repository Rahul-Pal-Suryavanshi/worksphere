package com.worksphere.api.dto;

import com.worksphere.api.enums.WorkspaceRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRoleRequest {

    private WorkspaceRole role;
}
