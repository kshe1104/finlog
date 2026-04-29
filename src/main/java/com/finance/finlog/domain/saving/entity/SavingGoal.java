package com.finance.finlog.domain.saving.entity;

import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "saving_goals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SavingGoal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal targetAmount;

    // 만약 currentAmount 저장 안한다면 매번 계산해야함
    // BigDecimal current = transactionRepository.sumAmountByGoalId(goalId);
    // 전체 거래 조회 후 합산
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentAmount;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status;


    // 저축 금액 추가
    public void addAmount(BigDecimal amount) {
        this.currentAmount = this.currentAmount.add(amount);
        // 금액 추가와 동시에 완료 여부 체크 -> 항상 같이 실행되는 게 보장 => 비즈니스 규칙을 엔티티안에 캡슐화한다.
        if (this.currentAmount.compareTo(this.targetAmount) >= 0) {
            this.status = GoalStatus.COMPLETED;
        }
    }

    // 목표 기한 만료 처리
    public void expire() {
        if (this.status == GoalStatus.IN_PROGRESS) {
            this.status = GoalStatus.FAILED;
        }
    }

    // 달성률 계산
    public int getAchievementRate() {
        if (targetAmount.compareTo(BigDecimal.ZERO) == 0) return 0;
        return currentAmount
                .multiply(BigDecimal.valueOf(100))
                .divide(targetAmount, 0, java.math.RoundingMode.DOWN)
                .intValue();
    }
    // BigDecimal 비교는 항상 CompareTo 사용해야함. 값이 같아도 소수점 자릿수가 다르다면 false 반환해서 문제가생김
}
