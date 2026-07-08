package com.worksphere.api.dto;

import com.worksphere.api.enums.TaskPriority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    private String title;

    private String description;

    private TaskPriority priority;

    private String assignedUserEmail;
}
