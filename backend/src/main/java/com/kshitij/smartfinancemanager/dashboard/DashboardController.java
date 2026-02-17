package com.kshitij.smartfinancemanager.dashboard;

import com.kshitij.smartfinancemanager.common.ApiResponse;
import com.kshitij.smartfinancemanager.dashboard.dto.DashboardSummaryResponse;
import com.kshitij.smartfinancemanager.dashboard.dto.MonthlySummaryResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        return dashboardService.getDashboardSummary();
    }
    
    @GetMapping("/monthly")
    public ApiResponse<List<MonthlySummaryResponse>> getMonthlySummary() {
        return dashboardService.getMonthlySummary();
    }

}