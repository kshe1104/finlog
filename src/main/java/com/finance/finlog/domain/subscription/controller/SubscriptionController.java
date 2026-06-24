package com.finance.finlog.domain.subscription.controller;

import com.finance.finlog.domain.subscription.dto.SubscriptionRequest;
import com.finance.finlog.domain.subscription.dto.SubscriptionResponse;
import com.finance.finlog.domain.subscription.service.SubscriptionService;
import com.finance.finlog.global.common.CommonResponse;
import com.finance.finlog.global.common.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "구독 관리", description = "구독 관련 API")
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "구독 목록 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<List<SubscriptionResponse>>> getSubscriptions(
            @CurrentUser Long userId) {

        List<SubscriptionResponse> result =
                subscriptionService.getSubscriptions(userId);
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @Operation(summary = "구독 생성")
    @PostMapping
    public ResponseEntity<CommonResponse<SubscriptionResponse>> createSubscription(
            @CurrentUser Long userId,
            @Valid @RequestBody SubscriptionRequest request) {

        SubscriptionResponse result =
                subscriptionService.createSubscription(userId, request);
        return ResponseEntity.ok(CommonResponse.success(result, "구독이 등록됐어요"));
    }

    @Operation(summary = "구독 목록 수정")
    @PutMapping("/{subscriptionId}")
    public ResponseEntity<CommonResponse<SubscriptionResponse>> updateSubscription(
            @CurrentUser Long userId,
            @PathVariable Long subscriptionId,
            @Valid @RequestBody SubscriptionRequest request) {

        SubscriptionResponse result =
                subscriptionService.updateSubscription(userId, subscriptionId, request);
        return ResponseEntity.ok(CommonResponse.success(result, "구독이 수정됐어요"));
    }

    @Operation(summary = "구독 목록 부분수정")
    @PatchMapping("/{subscriptionId}/toggle")
    public ResponseEntity<CommonResponse<SubscriptionResponse>> toggleSubscription(
            @CurrentUser Long userId,
            @PathVariable Long subscriptionId) {

        SubscriptionResponse result =
                subscriptionService.toggleSubscription(userId, subscriptionId);
        return ResponseEntity.ok(CommonResponse.success(result, "구독 상태가 변경됐어요"));
    }

    @Operation(summary = "구독 목록 살제")
    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<CommonResponse<Void>> deleteSubscription(
            @CurrentUser Long userId,
            @PathVariable Long subscriptionId) {

        subscriptionService.deleteSubscription(userId, subscriptionId);
        return ResponseEntity.ok(CommonResponse.success(null, "구독이 삭제됐어요"));
    }
}