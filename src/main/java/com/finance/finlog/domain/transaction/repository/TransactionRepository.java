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
    // findById만 하면 남의 가계부내역이어도 ID번호만 맞으면 조회되어버림
    // ANDUser 으로 지금 로그인한 유저의 거래내역만 가져오기
}
