package com.finance.finlog.domain.transaction.repository;

import com.finance.finlog.domain.transaction.entity.TransactionType;
import com.finance.finlog.domain.user.entity.User;

import java.math.BigDecimal;

public interface TransactionRepositoryCustom {
    BigDecimal sumAmountByUserAndTypeAndYearMonth(User user, TransactionType type, int year, int month);
}
