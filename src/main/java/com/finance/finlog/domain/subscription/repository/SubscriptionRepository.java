package com.finance.finlog.domain.subscription.repository;

import com.finance.finlog.domain.subscription.entity.Subscription;
import com.finance.finlog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUser(User user);

    List<Subscription> findAllByUserAndIsActive(User user, boolean isActive);

    // 스케줄러 용 - 오늘 결제일인 활성 구독 전체 조회
    List<Subscription> findAllByNextBillingDateAndIsActiveTrue(LocalDate nextBillingDate);

    Optional<Subscription> findByIdAndUser(Long id, User user);

}
