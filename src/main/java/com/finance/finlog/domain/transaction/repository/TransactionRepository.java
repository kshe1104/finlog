package com.finance.finlog.domain.transaction.repository;

import com.finance.finlog.domain.transaction.entity.Transaction;
import com.finance.finlog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserAndTransactionDateBetweenOrderByTransactionDateDesc(User user, LocalDate start, LocalDate end);

    Optional<Transaction> findByIdAndUser(Long id, User user);

    // 월별 수입/지출 합계
    // COALESCE(SUM(...), 0) 인 이유 — 거래 내역이 하나도 없으면 SUM() 이 null 을 반환해요. COALESCE 는 null 이면 대신 0 을 반환해줘요. null 이 서비스 로직에 흘러들어오면 NullPointerException 이 날 수 있어서 미리 방어해요.
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user = :user " +
            "AND t.type = :type " +
            "AND YEAR(t.transactionDate) = :year " +
            "AND MONTH(t.transactionDate) = :month")
    // @Param 은 @Query 안에서 :user, :type 등과 연결해주는 역할
    BigDecimal sumAmountByUserAndTypeAndYearMonth(
            @Param("user") User user,
            @Param("type") String type,
            @Param("year") int year,
            @Param("month") int month);

}
