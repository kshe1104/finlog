package com.finance.finlog.domain.transaction.controller;

import com.finance.finlog.domain.transaction.dto.MonthlyStatResponse;
import com.finance.finlog.domain.transaction.dto.TransactionRequest;
import com.finance.finlog.domain.transaction.dto.TransactionResponse;
import com.finance.finlog.domain.transaction.service.TransactionService;
import com.finance.finlog.global.common.CommonResponse;
import com.finance.finlog.global.common.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "거래내역",description = "거래 내역 관련 API")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "거래 내역 조회")
    @GetMapping // 이번 달 요약 목록조회
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getTransactions(
            @CurrentUser Long userId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}")
            int year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}")
            int month) {

        List<TransactionResponse> result =
                transactionService.getTransactions(userId, year, month);
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @Operation(summary = "이번 달 거래 내역 조회")
    @GetMapping("/stats") // 이번 달 상세 수입 및 지출
    public ResponseEntity<CommonResponse<MonthlyStatResponse>> getMonthlyStat(
            @CurrentUser Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        int y = year != null ? year : LocalDate.now().getYear();
        int m = month != null ? month : LocalDate.now().getMonthValue();

        MonthlyStatResponse result = transactionService.getMonthlyStat(userId, y, m);
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @Operation(summary = "거래 생성")
    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransaction(
            @CurrentUser Long userId,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse result =
                transactionService.createTransaction(userId, request);
        return ResponseEntity.ok(CommonResponse.success(result, "거래가 등록됐어요"));
    }

    @Operation(summary = "거래 내역 수정")
    @PutMapping("/{transactionId}")
    public ResponseEntity<CommonResponse<TransactionResponse>> updateTransaction(
            @CurrentUser Long userId,
            @PathVariable Long transactionId,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse result =
                transactionService.updateTransaction(userId, transactionId, request);
        return ResponseEntity.ok(CommonResponse.success(result, "거래가 수정됐어요"));
    }

    @Operation(summary = "거래 삭제")
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteTransaction(
            @CurrentUser Long userId,
            @PathVariable Long transactionId) {

        transactionService.deleteTransaction(userId, transactionId);
        return ResponseEntity.ok(CommonResponse.success(null, "거래가 삭제됐어요"));
    }
}
