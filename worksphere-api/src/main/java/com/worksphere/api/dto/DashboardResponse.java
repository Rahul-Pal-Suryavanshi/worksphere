package com.worksphere.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private String workspaceName;
    private long totalProjects;
    private long totalTasks;
    private long todoTasks;
    private long inProgresstasks;
    private long doneTasks;
    private double completionPercentage;
}
