package com.finance.finlog.domain.subscription.entity;

import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private int billingDay; // 매월 결제일 (1~31)

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private LocalDate nextBillingDate; // billingDay랑 별도로 저장하는 이유
    // 미리 다음 결제일을 저장해서 스케줄러가 매일 `nextBillingDate = 오늘` 인 구독을 조회해서 처리하면 되기 때문에 쿼리가 단순해짐
    // ex) subscriptionRepository.findAllByNextBillingDateAndIsActiveTrue(LocalDate.now());

    //Setter 대신 의도가 명확한 메서드 사용

    // 구독 활성/비활성 토글
    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    // 다음 결제일 갱신
    public void updateNextBillingDate(LocalDate nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }


    public void update(String name, BigDecimal amount, int billingDay,
                       LocalDate nextBillingDate, Category category) {
        this.name = name;
        this.amount = amount;
        this.billingDay = billingDay;
        this.nextBillingDate = nextBillingDate;
        this.category = category;
    }
}
