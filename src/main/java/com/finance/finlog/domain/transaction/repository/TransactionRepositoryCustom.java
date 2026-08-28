package com.finance.finlog.domain.transaction.repository;

import com.finance.finlog.domain.transaction.entity.TransactionType;
import com.finance.finlog.domain.user.entity.User;

import java.math.BigDecimal;

// 복잡한 조건이나 합계(SUM), 평균(AVG) 같은 집계연산 시 사용하는 인터페이스
public interface TransactionRepositoryCustom {
    BigDecimal sumAmountByUserAndTypeAndYearMonth(User user, TransactionType type, int year, int month);
}
