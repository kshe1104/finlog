package com.finance.finlog.domain.category.repository;

import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.category.entity.CategoryType;
import com.finance.finlog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUser(User user);

    // 수입 or 지출 카테고리만 필터링해서 가져옴
    List<Category> findAllByUserAndType(User user, CategoryType type);

    // IDOR 방어 , 카테고리 수정/삭제 시 본인 것만 건드릴 수 있도록 조건을 id+user 로 만듦
    Optional<Category> findByIdAndUser(Long id, User user);

    // 같은 유저가 같은 이름의 카테고리를 중복으로 만들지 못하게 막음
    boolean existsByUserAndName(User user, String name);
}
