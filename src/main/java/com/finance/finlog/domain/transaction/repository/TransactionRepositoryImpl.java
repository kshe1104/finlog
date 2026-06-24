package com.finance.finlog.domain.transaction.repository;

import com.finance.finlog.domain.transaction.entity.QTransaction;
import com.finance.finlog.domain.transaction.entity.TransactionType;
import com.finance.finlog.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private static final QTransaction transaction = QTransaction.transaction;

    @Override
    public BigDecimal sumAmountByUserAndTypeAndYearMonth(User user, TransactionType type, int year, int month) {
        BigDecimal result = queryFactory
                .select(transaction.amount.sum())
                .from(transaction)
                .where(
                        transaction.user.eq(user),
                        transaction.type.eq(type),
                        transaction.transactionDate.year().eq(year),
                        transaction.transactionDate.month().eq(month)
                )
                .fetchOne();

        return result != null ? result : BigDecimal.ZERO;
    }
}
