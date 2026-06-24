package com.finance.finlog.domain.saving.service;

import com.finance.finlog.domain.saving.dto.AddAmountRequest;
import com.finance.finlog.domain.saving.dto.SavingGoalRequest;
import com.finance.finlog.domain.saving.dto.SavingGoalResponse;
import com.finance.finlog.domain.saving.entity.GoalStatus;
import com.finance.finlog.domain.saving.entity.SavingGoal;
import com.finance.finlog.domain.saving.repository.SavingGoalRepository;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.domain.user.repository.UserRepository;
import com.finance.finlog.global.common.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavingGoalService {

    private final SavingGoalRepository savingGoalRepository;
    private final UserRepository userRepository;

    // 목표 전체 조회
    public List<SavingGoalResponse> getSavingGoals(Long userId) {
        User user = getUser(userId);
        return savingGoalRepository.findAllByUser(user)
                .stream()
                .map(SavingGoalResponse::from)
                .toList();
    }

    // 진행중인 목표만 조회
    public List<SavingGoalResponse> getInProgressGoals(Long userId) {
        User user = getUser(userId);
        return savingGoalRepository.findAllByUserAndStatus(user, GoalStatus.IN_PROGRESS).stream().map(SavingGoalResponse::from).toList();
    }

    // 목표 생성
    @Transactional
    public SavingGoalResponse createSavingGoal(Long userId, SavingGoalRequest request){
        User user = getUser(userId);

        SavingGoal savingGoal = SavingGoal.builder()
                .user(user)
                .name(request.getName())
                .targetAmount(request.getTargetAmount())
                .currentAmount(BigDecimal.ZERO) // 만약 null이라면 예외 발생할 수 있어서 0으로 초기화
                .deadline(request.getDeadline())
                .status(GoalStatus.IN_PROGRESS)
                .build();

        return SavingGoalResponse.from(savingGoalRepository.save(savingGoal));
    }

    // 저축 금액 추가
    @Transactional
    public SavingGoalResponse addAmount(Long userId, Long goalId, AddAmountRequest request) {
        User user = getUser(userId);
        SavingGoal savingGoal = savingGoalRepository.findByIdAndUser(goalId, user)
                .orElseThrow(() -> BusinessException.notFound("목표를 찾을 수 없습니다"));

        if (savingGoal.getStatus() != GoalStatus.IN_PROGRESS) {
            throw BusinessException.badRequest("진행중인 목표에만 금액추가가 가능합니다");
        }

        savingGoal.addAmount(request.getAmount());
        return SavingGoalResponse.from(savingGoal);
        // save() 하지 않아도 @Transactional 안에서 엔티티를 수정하면 트랜잭션 종료시 자동으로 UPDATE 쿼리가 나옴(변경감지)
    }

    // 목표 삭제
    @Transactional
    public void deleteSavingGoal(Long userId, Long goalId) {
        User user = getUser(userId);
        SavingGoal savingGoal = savingGoalRepository.findByIdAndUser(goalId, user)
                .orElseThrow(() -> BusinessException.notFound("목표를 찾을 수 없습니다"));
        savingGoalRepository.delete(savingGoal);
    }


    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> BusinessException.notFound("유저를 찾을 수 없습니다"));
    }
}
