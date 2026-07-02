package com.worksphere.api.dto;

import lombok.*;


import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {

    private UUID id;

    private String name;

    private String description;

    private String workspaceName;
}
