package com.finance.finlog.domain.subscription.service;

import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.category.repository.CategoryRepository;
import com.finance.finlog.domain.subscription.dto.SubscriptionRequest;
import com.finance.finlog.domain.subscription.dto.SubscriptionResponse;
import com.finance.finlog.domain.subscription.entity.Subscription;
import com.finance.finlog.domain.subscription.repository.SubscriptionRepository;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.domain.user.repository.UserRepository;
import com.finance.finlog.global.common.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // 구독 목록 조회
    public List<SubscriptionResponse> getSubscriptions(Long userId){
        User user = getUser(userId);
        return subscriptionRepository.findAllByUser(user)
                .stream()
                .map(SubscriptionResponse::from)
                .toList();
    }

    // 구독 등록
    @Transactional
    public SubscriptionResponse createSubscription(Long userId, SubscriptionRequest request) {
        User user = getUser(userId);
        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> BusinessException.notFound("카테고리를 찾을 수 없습니다"));

        LocalDate nextBillingDate = calculateNextBillingDate(request.getBillingDay());
        Subscription subscription = Subscription.builder()
                .user(user)
                .category(category)
                .name(request.getName())
                .amount(request.getAmount())
                .billingDay(request.getBillingDay())
                .isActive(true)
                .nextBillingDate(nextBillingDate)
                .build();

        return SubscriptionResponse.from(subscriptionRepository.save(subscription));
    }

    // 구독 수정
    @Transactional
    public SubscriptionResponse updateSubscription(Long userId, Long subscriptionId,
                                                   SubscriptionRequest request) {
        User user = getUser(userId);
        Subscription subscription = subscriptionRepository
                .findByIdAndUser(subscriptionId, user)
                .orElseThrow(() -> BusinessException.notFound("구독을 찾을 수 없어요"));

        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> BusinessException.notFound("카테고리를 찾을 수 없어요"));

        LocalDate nextBillingDate = calculateNextBillingDate(request.getBillingDay());
        subscription.update(request.getName(), request.getAmount(),
                request.getBillingDay(), nextBillingDate, category);

        return SubscriptionResponse.from(subscription);
    }

    // 구독 활성/비활성 토글
    @Transactional
    public SubscriptionResponse toggleSubscription(Long userId, Long subscriptionId) {
        User user = getUser(userId);
        Subscription subscription = subscriptionRepository
                .findByIdAndUser(subscriptionId, user)
                .orElseThrow(() -> BusinessException.notFound("구독을 찾을 수 없어요"));

        subscription.toggleActive();
        return SubscriptionResponse.from(subscription);
    }

    // 구독 삭제
    @Transactional
    public void deleteSubscription(Long userId, Long subscriptionId) {
        User user = getUser(userId);
        Subscription subscription = subscriptionRepository
                .findByIdAndUser(subscriptionId, user)
                .orElseThrow(() -> BusinessException.notFound("구독을 찾을 수 없어요"));

        subscriptionRepository.delete(subscription);
    }


    // 다음 결제일 계산
    private LocalDate calculateNextBillingDate(int billingDay) {
        LocalDate today = LocalDate.now();
        LocalDate thisMonthBillingDate = today.withDayOfMonth(
                Math.min(billingDay, today.lengthOfMonth())); // 해당 월의 마지막 날을 넘지 않도록

        // 이번 달 결제일이 오늘 이후면 이번 달, 아니면 다음 달
        if (!thisMonthBillingDate.isBefore(today)) {
            return thisMonthBillingDate;
        }

        LocalDate nextMonth = today.plusMonths(1);
        return nextMonth.withDayOfMonth(
                Math.min(billingDay, nextMonth.lengthOfMonth()));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> BusinessException.notFound("유저를 찾을 수 없어요"));
    }
}
