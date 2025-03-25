package com.voting.kura.controller;

import com.voting.kura.dto.response.CandidateMetricsResponse;
import com.voting.kura.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/position/{positionId}/metrics")
    public ResponseEntity<CandidateMetricsResponse> getPositionMetrics(@PathVariable Long positionId) {
        return ResponseEntity.ok(dashboardService.getPositionMetrics(positionId));
    }
}