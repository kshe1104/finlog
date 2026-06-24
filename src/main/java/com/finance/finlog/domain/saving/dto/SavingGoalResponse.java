package com.finance.finlog.domain.saving.dto;

import com.finance.finlog.domain.saving.entity.GoalStatus;
import com.finance.finlog.domain.saving.entity.SavingGoal;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class SavingGoalResponse {    private final Long id;
    private final String name;
    private final BigDecimal targetAmount;
    private final BigDecimal currentAmount;
    private final int achievementRate;
    private final LocalDate deadline;
    private final GoalStatus status;

    public SavingGoalResponse(SavingGoal savingGoal) {
        this.id = savingGoal.getId();
        this.name = savingGoal.getName();
        this.targetAmount = savingGoal.getTargetAmount();
        this.currentAmount = savingGoal.getCurrentAmount();
        this.achievementRate = savingGoal.getAchievementRate();
        this.deadline = savingGoal.getDeadline();
        this.status = savingGoal.getStatus();
    }

    public static SavingGoalResponse from(SavingGoal savingGoal) {
        return new SavingGoalResponse(savingGoal);
    }
}
