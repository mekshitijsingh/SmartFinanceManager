package com.kshitij.smartfinancemanager.finance;

import com.kshitij.smartfinancemanager.common.ApiResponse;
import com.kshitij.smartfinancemanager.exception.ResourceNotFoundException;
import com.kshitij.smartfinancemanager.exception.UnauthorizedException;
import com.kshitij.smartfinancemanager.finance.dto.TransactionRequest;
import com.kshitij.smartfinancemanager.finance.dto.TransactionPageResponse;
import com.kshitij.smartfinancemanager.finance.dto.TransactionResponse;
import com.kshitij.smartfinancemanager.security.CustomUserDetails;
import com.kshitij.smartfinancemanager.user.User;
import com.kshitij.smartfinancemanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public ApiResponse<TransactionResponse> createTransaction(TransactionRequest request) {

        // 1️⃣ Get logged-in user email from SecurityContext
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        // 2️⃣ Fetch full User entity from DB
        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 3️⃣ Create transaction linked to that user
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .description(request.getDescription())
                .date(request.getDate())
                .user(user)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        // 4️⃣ Convert to response DTO
        TransactionResponse response = TransactionResponse.builder()
                .id(saved.getId())
                .amount(saved.getAmount())
                .type(saved.getType())
                .category(saved.getCategory())
                .description(saved.getDescription())
                .date(saved.getDate())
                .build();

        return ApiResponse.success("Transaction created successfully", response);
    }


    public ApiResponse<TransactionPageResponse> getMyTransactions(
            int page,
            int size,
            String sortBy,
            String direction,
            LocalDate startDate,
            LocalDate endDate
    ) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Transaction> transactionPage;

        if (startDate != null && endDate != null) {
            transactionPage = transactionRepository
                    .findByUserAndDateBetween(user, startDate, endDate, pageable);
        } else {
            transactionPage = transactionRepository
                    .findByUser(user, pageable);
        }

        List<TransactionResponse> content =
                transactionPage.getContent()
                        .stream()
                        .map(tx -> TransactionResponse.builder()
                                .id(tx.getId())
                                .amount(tx.getAmount())
                                .type(tx.getType())
                                .category(tx.getCategory())
                                .description(tx.getDescription())
                                .date(tx.getDate())
                                .build()
                        )
                        .toList();

        TransactionPageResponse response =
                TransactionPageResponse.builder()
                        .content(content)
                        .currentPage(transactionPage.getNumber())
                        .totalPages(transactionPage.getTotalPages())
                        .totalElements(transactionPage.getTotalElements())
                        .pageSize(transactionPage.getSize())
                        .build();

        return ApiResponse.success("Transactions fetched successfully", response);
    }

    public void exportTransactions(HttpServletResponse response) throws IOException {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUser(user);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.csv");

        PrintWriter writer = response.getWriter();

        // CSV Header
        writer.println("Date,Type,Amount,Category,Description");

        // CSV Data
        for (Transaction tx : transactions) {
            writer.printf("%s,%s,%s,%s,%s%n",
                    tx.getDate(),
                    tx.getType(),
                    tx.getAmount(),
                    tx.getCategory(),
                    tx.getDescription() != null ? tx.getDescription() : ""
            );
        }

        writer.flush();
    }
    
    public void deleteTransaction(Long id, String email) {

        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Transaction transaction = transactionRepository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
        	throw new UnauthorizedException("You are not allowed to access this resource");
        }

        transactionRepository.delete(transaction);
    }

    public void updateTransaction(Long id, TransactionRequest request, String email) {

        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Transaction transaction = transactionRepository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
        	throw new UnauthorizedException("You are not allowed to access this resource");
        }

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());

        transactionRepository.save(transaction);
    }




}
