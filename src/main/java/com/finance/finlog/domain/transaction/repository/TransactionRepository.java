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

public interface TransactionRepository extends JpaRepository<Transaction, Long> ,TransactionRepositoryCustom{
    // 월별 거래 내역 조회(최신순)
    // SELECT * FROM transactions
    // WHERE user_id = ?
    // AND transaction_date BETWEEN ? AND ?
    // ORDER BY transaction_date DESC
    List<Transaction> findAllByUserAndTransactionDateBetweenOrderByTransactionDateDesc(User user, LocalDate start, LocalDate end);

    Optional<Transaction> findByIdAndUser(Long id, User user);

}
