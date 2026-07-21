package com.worksphere.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceRequest {

    @NotBlank(message = "Workspace name is required")
    @Size(min = 3, max = 100)
    private String name;
}
