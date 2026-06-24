package com.finance.finlog.domain.saving.repository;

import com.finance.finlog.domain.saving.entity.GoalStatus;
import com.finance.finlog.domain.saving.entity.SavingGoal;
import com.finance.finlog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SavingGoalRepository extends JpaRepository<SavingGoal, Long> {
    List<SavingGoal> findAllByUser(User user);

    List<SavingGoal> findAllByUserAndStatus(User user, GoalStatus status);

    // 스케줄러용 - 기한 지난 진행중인 목표 조회
    // Before은 <? 조건을 나타낸다.
    List<SavingGoal> findAllByStatusAndDeadlineBefore(GoalStatus status, LocalDate date);

    Optional<SavingGoal> findByIdAndUser(Long id, User user);
}
