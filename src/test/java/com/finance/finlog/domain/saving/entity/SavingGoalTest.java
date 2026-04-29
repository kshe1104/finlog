package com.finance.finlog.domain.saving.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SavingGoalTest {

    @Test
    void 저축금액추가시목표달성하면COMPLETE로변경(){
        //given
        SavingGoal goal = SavingGoal.builder()
                .targetAmount(new BigDecimal("100000"))
                .currentAmount(new BigDecimal("0"))
                .status(GoalStatus.IN_PROGRESS)
                .build();

        // when
        goal.addAmount(new BigDecimal("1000000"));

        //then
        assertThat(goal.getStatus()).isEqualTo(GoalStatus.COMPLETED);
    }

}