package com.finance.finlog.global.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;

    public CommonResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // 생성자를 직접 사용하기보다
    // 정적 팩토리 메서드 CommonResponse.success(data) 가 더 직관적임

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true,data, "성공");
    }

    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(true, data, message);
    }

    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>(false, null, message);
    }
}


