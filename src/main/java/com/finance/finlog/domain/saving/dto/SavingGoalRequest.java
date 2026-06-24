package com.finance.finlog.domain.saving.dto;

import com.finance.finlog.domain.saving.entity.GoalStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class SavingGoalRequest {

    @NotBlank(message = "목표 이름을 입력하세요")
    private String name;

    @NotNull(message = "목표 금액을 입력하세요")
    @Positive(message = "목표 금액은 0보다 커야합니다")
    private BigDecimal targetAmount;

    @NotNull(message = "목표 기한을 입력하세요")
    @Future(message = "목표 기한은 오늘 이후여야 합니다") // 목표이기에 과거로 만들지 못하게
    private LocalDate deadline;
}
