package com.worksphere.api.dto;

import com.worksphere.api.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatusRequest {

    private TaskStatus status;
}
