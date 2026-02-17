package com.kshitij.smartfinancemanager.finance;

import com.kshitij.smartfinancemanager.common.ApiResponse;
import com.kshitij.smartfinancemanager.finance.dto.TransactionPageResponse;
import com.kshitij.smartfinancemanager.finance.dto.TransactionRequest;
import com.kshitij.smartfinancemanager.finance.dto.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ApiResponse<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request
  ) {
        return transactionService.createTransaction(request);
    }

    @GetMapping
    public ApiResponse<TransactionPageResponse> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return transactionService.getMyTransactions(
                page,
                size,
                sortBy,
                direction,
                startDate,
                endDate
        );
    }

    @GetMapping("/export")
    public void exportTransactions(HttpServletResponse response) throws IOException {
        transactionService.exportTransactions(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteTransaction(
            @PathVariable Long id,
            Authentication authentication
    ) {
        transactionService.deleteTransaction(id, authentication.getName());
        return ApiResponse.success("Transaction deleted successfully", null);
    }
    
    @PutMapping("/{id}")
    public ApiResponse<String> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequest request,
            Authentication authentication
    ) {
        transactionService.updateTransaction(id, request, authentication.getName());
        return ApiResponse.success("Transaction updated successfully", null);
    }





}