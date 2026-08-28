package com.finance.finlog.domain.transaction.repository;

import com.finance.finlog.domain.transaction.entity.QTransaction;
import com.finance.finlog.domain.transaction.entity.TransactionType;
import com.finance.finlog.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
// TransactionRepositoryCustom을 실제로 동작하게 만드는 구현체
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    // QueryDsl의 핵심도구, 자바 코드로 DB 쿼리문을 작성할 수 있게 해주는 쿼리공장
    private final JPAQueryFactory queryFactory;

    // JPA의 Transaction 엔티티를 보고 QTransaction 전용 자바 클래스로 만들어줌
    private static final QTransaction transaction = QTransaction.transaction;


    @Override
    public BigDecimal sumAmountByUserAndTypeAndYearMonth(User user, TransactionType type, int year, int month) {

        BigDecimal result = queryFactory
                .select(transaction.amount.sum()) // 금액(amount) 다 더하기(SUM)
                .from(transaction) // Transaction 테이블에서
                .where(
                        transaction.user.eq(user), // 유저가 일치
                        transaction.type.eq(type), // 타입(거래 유형(수입/지출)) 일치
                        transaction.transactionDate.year().eq(year), // 연도 일치
                        transaction.transactionDate.month().eq(month) // 월이 일치
                )
                .fetchOne(); // 계산결과 1개만 가져와!

        return result != null ? result : BigDecimal.ZERO; // NullpointException 미리처리
    }
}
