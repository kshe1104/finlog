package com.finance.finlog.domain.saving.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AddAmountRequest { // 저축금액 추가 시 amount만 받으면됨
    // name,targetAmount,deadline은 필요 없다.
    // 이런 경우, 별도 DTO를 만드는 게 낫다.
    @NotNull(message = "금액을 입력하세요")
    @Positive(message = "금액은 0보다 커야합니다")
    private BigDecimal amount;
}
