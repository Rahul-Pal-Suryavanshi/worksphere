package com.worksphere.api.dto;

import com.worksphere.api.enums.WorkspaceRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class WorkspaceMemberResponse {

    private UUID memberId;
    private String workspaceName;
    private String memberName;
    private String memberEmail;
    private WorkspaceRole role;

}
