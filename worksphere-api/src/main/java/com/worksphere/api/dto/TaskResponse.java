package com.worksphere.api.dto;

import com.worksphere.api.enums.TaskPriority;
import com.worksphere.api.enums.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {

    private UUID id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private String assignedUserEmail;

    private String projectName;

    private LocalDateTime createdAt;
}
