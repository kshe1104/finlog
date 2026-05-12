package com.finance.finlog.global.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController // 모든 Controller 예외를 한곳에서 처리해주는 클래스
public class GlobalExceptionHandler {

    // 직접 만든 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: {}", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(CommonResponse.fail(e.getMessage()));
    }

    // @Valid 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("입력값이 올바르지 않아요");
        log.warn("ValidationException: {}", message);
        return ResponseEntity.badRequest()
                .body(CommonResponse.fail(message));
    }

    // 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e){
        log.error("Unexpected Exception: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(CommonResponse.fail("서버 오류가 발생했어요"));
    }
}
