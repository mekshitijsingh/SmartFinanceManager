package com.kshitij.smartfinancemanager.dashboard;

import com.kshitij.smartfinancemanager.common.ApiResponse;
import com.kshitij.smartfinancemanager.dashboard.dto.DashboardSummaryResponse;
import com.kshitij.smartfinancemanager.finance.TransactionRepository;
import com.kshitij.smartfinancemanager.security.CustomUserDetails;
import com.kshitij.smartfinancemanager.user.User;
import com.kshitij.smartfinancemanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import com.kshitij.smartfinancemanager.dashboard.dto.MonthlySummaryResponse;
import com.kshitij.smartfinancemanager.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public ApiResponse<DashboardSummaryResponse> getDashboardSummary() {

        // 1️⃣ Get logged-in user from SecurityContext
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2️⃣ Fetch totals from repository
        BigDecimal totalIncome = transactionRepository.getTotalIncome(user);
        BigDecimal totalExpense = transactionRepository.getTotalExpense(user);

        // 3️⃣ Calculate net savings
        BigDecimal netSavings = totalIncome.subtract(totalExpense);

        DashboardSummaryResponse response =
                DashboardSummaryResponse.builder()
                        .totalIncome(totalIncome)
                        .totalExpense(totalExpense)
                        .netSavings(netSavings)
                        .build();

        return ApiResponse.success("Dashboard summary fetched successfully", response);
    }
    
    public ApiResponse<List<MonthlySummaryResponse>> getMonthlySummary() {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<MonthlySummaryResponse> monthlySummary =
                transactionRepository.getMonthlySummary(user);

        return ApiResponse.success("Monthly summary fetched successfully", monthlySummary);
    }

}
