package com.finance.finlog.global.common;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 어노테이션 커스텀
// @AuthenticationPrincipal과 같이 Spring Security를 한번 더 고민하지 않아도 됨, 비즈니스 용어로 이해가 직관적이고
// 나중에 유지보수도 용이하다. 해당 커스텀된 어노테이션 내부만 수정하면됨.
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
public @interface CurrentUser {
}
