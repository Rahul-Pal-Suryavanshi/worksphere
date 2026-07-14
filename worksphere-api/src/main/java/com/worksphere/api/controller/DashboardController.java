package com.worksphere.api.controller;

import com.worksphere.api.dto.DashboardResponse;
import com.worksphere.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{workspaceId}")
    public DashboardResponse getDashboard(
            @PathVariable UUID workspaceId,
            Authentication authentication
            ){

        return dashboardService.getDashboard(workspaceId, authentication);
    }
}
