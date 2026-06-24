package com.finance.finlog.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 커스텀 예외가 없으면 예외마다 상태코드를 직접 지정해야함.
// But, 커스텀 예외가 있으니 예외만 던지면됨
@Getter
public class BusinessException extends RuntimeException{
    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    // 자주 쓰는 예외 정적 팩토리 메서드
    public static BusinessException notFound(String message) {
        return new BusinessException(message, HttpStatus.NOT_FOUND);
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(message, HttpStatus.BAD_REQUEST);
    }

    public static BusinessException forbidden(String message) {
        return new BusinessException(message, HttpStatus.FORBIDDEN);
    }
}
