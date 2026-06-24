package com.finance.finlog.global.common;

import lombok.Getter;

@Getter // 공통된 API응답 형식
public class CommonResponse<T> {
    private final boolean success;
    private final T data; // 어떤 데이터 형태라도 받을 수 있게 T
    private final String message;

    private CommonResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // 생성자를 private로 막고 정적팩토리 메서드를 사용하나면
    // 정적 팩토리 메서드 CommonResponse.success(data) 가 더 직관적임

    // <T> 제네릭인 이유는 데이터가 API마다 다를 수 있기 때문

    // 데이터만 들어오면 기본 메세지인 성공을 얹어서 리턴
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true,data, "성공");
    }

    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(true, data, message);
    }
    // 어떤 상황에서 성공할지 모르기때문에 재사용성을 높이기 위해 하나 오버로딩함

    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>(false, null, message);
    }
}


