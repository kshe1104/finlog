package com.finance.finlog.domain.saving.controller;

import com.finance.finlog.domain.saving.dto.AddAmountRequest;
import com.finance.finlog.domain.saving.dto.SavingGoalRequest;
import com.finance.finlog.domain.saving.dto.SavingGoalResponse;
import com.finance.finlog.domain.saving.service.SavingGoalService;
import com.finance.finlog.global.common.CommonResponse;
import com.finance.finlog.global.common.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "목표 저축",description = "목표 저축 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saving-goals")
public class SavingGoalController {
    private final SavingGoalService savingGoalService;

    @Operation(summary = "목표 저축금액 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<List<SavingGoalResponse>>> getSavingGoals(@CurrentUser Long userId, @RequestParam(required = false) Boolean inProgress) {
        // inProgress == true로 하면 inProgress가 null일 경우, null==true는 비교가 안되기 때문에 NullPointerException 예외 발생,
        // equals로 바꾸면, null일 경우 false 반환
        List<SavingGoalResponse> result = Boolean.TRUE.equals(inProgress) ? savingGoalService.getInProgressGoals(userId) : savingGoalService.getSavingGoals(userId);

        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @Operation(summary = "목표 저축금액 생성")
    @PostMapping
    public ResponseEntity<CommonResponse<SavingGoalResponse>> createSavingGoal(@CurrentUser Long userId, @Valid @RequestBody SavingGoalRequest request) {
        SavingGoalResponse result = savingGoalService.createSavingGoal(userId, request);
        return ResponseEntity.ok(CommonResponse.success(result, "목표가 등록됐습니다"));
    }

    @Operation(summary = "목표 금액 추가")
    @PatchMapping("{goalId}/amount")
    public ResponseEntity<CommonResponse<SavingGoalResponse>> addAmount(@CurrentUser Long userId, @PathVariable Long goalId, @Valid @RequestBody AddAmountRequest request){
        SavingGoalResponse result = savingGoalService.addAmount(userId, goalId, request);
        return ResponseEntity.ok(CommonResponse.success(result, "저축 금액이 추가됐습니다"));
    }

    @Operation(summary = "목표 금액 삭제")
    @DeleteMapping("{goalId}")
    public ResponseEntity<CommonResponse<Void>> deleteSavingGoal(@CurrentUser Long userId, @PathVariable Long goalId) {
        savingGoalService.deleteSavingGoal(userId, goalId);
        return ResponseEntity.ok(CommonResponse.success(null, "목표가 삭제됐습니다"));
    }
}
