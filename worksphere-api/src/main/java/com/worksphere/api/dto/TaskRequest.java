package com.worksphere.api.dto;

import com.worksphere.api.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    @Size(min = 3,max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private TaskPriority priority;

    private String assignedUserEmail;
}
