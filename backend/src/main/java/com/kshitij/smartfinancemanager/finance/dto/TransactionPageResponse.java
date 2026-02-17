package com.kshitij.smartfinancemanager.finance.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TransactionPageResponse {

    private List<TransactionResponse> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
}