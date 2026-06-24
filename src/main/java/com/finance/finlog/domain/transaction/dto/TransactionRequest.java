package com.finance.finlog.domain.transaction.dto;

import com.finance.finlog.domain.transaction.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class TransactionRequest {
    @NotNull(message = "거래 유형을 선택하세요")
    private TransactionType type;

    @NotNull(message = "금액을 입력하세요")
    @Positive(message = "금액은 0보다 커야합니다")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "거래 날짜를 입력해주세요")
    private LocalDate transactionDate;

    @NotNull(message = "카테고리를 선택해주세요")
    private Long categoryId;
}
