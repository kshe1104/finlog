package com.finance.finlog.domain.transaction.dto;

import lombok.Getter;

import java.math.BigDecimal;

// 월별 통계 응답
@Getter
public class MonthlyStatResponse {

    private final int year;
    private final int month;
    private final BigDecimal totalIncome;
    private final BigDecimal totalExpense;
    private final BigDecimal balance;

    public MonthlyStatResponse(int year, int month,
                               BigDecimal totalIncome,
                               BigDecimal totalExpense) {
        this.year = year;
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        // 수입/지출을 알고 있으니까 스스로 계산
        // 비즈니스 로직을 객체 안에 캡슐화
        this.balance = totalIncome.subtract(totalExpense);
    }
}