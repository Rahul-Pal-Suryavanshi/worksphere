package com.worksphere.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(min = 3,max = 100)
    private String name;

    @Size(max = 500, message = "Description cannot exceed more than 500 characters")
    private String description;
}
