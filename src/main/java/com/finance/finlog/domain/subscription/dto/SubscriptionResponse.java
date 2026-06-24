package com.finance.finlog.domain.subscription.dto;

import com.finance.finlog.domain.subscription.entity.Subscription;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class SubscriptionResponse {
    private final Long id;
    private final String name;
    private final BigDecimal amount;
    private final int billingDay;
    private final boolean isActive;
    private final LocalDate nextBillingDate;
    private final long dday;
    private final String categoryName;

    public SubscriptionResponse(Subscription subscription) {
        this.id = subscription.getId();
        this.name = subscription.getName();
        this.amount = subscription.getAmount();
        this.billingDay = subscription.getBillingDay();
        this.isActive = subscription.isActive();
        this.nextBillingDate = subscription.getNextBillingDate();
        this.dday = ChronoUnit.DAYS.between(LocalDate.now(),subscription.getNextBillingDate()); // 오늘 기준으로 며칠 남았는지
        this.categoryName = subscription.getCategory().getName();
    }

    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(subscription);
    }
}
