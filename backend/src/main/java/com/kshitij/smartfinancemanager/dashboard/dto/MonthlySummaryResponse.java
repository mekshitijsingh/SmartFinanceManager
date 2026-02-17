package com.kshitij.smartfinancemanager.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MonthlySummaryResponse {

    private Integer year;
    private Integer month;
    private BigDecimal income;
    private BigDecimal expense;
}
