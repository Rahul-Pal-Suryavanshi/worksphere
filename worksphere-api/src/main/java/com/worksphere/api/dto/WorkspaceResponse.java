package com.worksphere.api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceResponse {

    private UUID id;
    private String name;
    private String ownerEmail;
}
