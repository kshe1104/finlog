package com.finance.finlog.domain.subscription.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SubscriptionRequest {
    @NotBlank(message = "구독 이름을 입력하세요")
    private String name;

    @NotNull(message = "금액을 입력하세요")
    @Positive(message = "금액은 0보다 커야 합니다")
    private BigDecimal amount;

    @NotNull(message = "결제일을 입력하세요")
    @Min(value = 1, message = "결제일은 1일 이상이어야 합니다")
    @Max(value = 31, message = "결제일은 31 이하여야 합니다")
    private Integer billingDay;

    @NotNull(message = "카테고리를 선택하세요")
    private Long categoryId;
}
