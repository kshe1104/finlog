package com.finance.finlog.domain.category.service;

import com.finance.finlog.domain.category.dto.CategoryRequest;
import com.finance.finlog.domain.category.dto.CategoryResponse;
import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.category.entity.CategoryType;
import com.finance.finlog.domain.category.repository.CategoryRepository;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.domain.user.repository.UserRepository;
import com.finance.finlog.global.common.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회가 대부분임(JPA의 변경감지 off)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // 카테고리 목록 조회
    public List<CategoryResponse> getCategories(Long userId) {
        User user = getUser(userId);
        return categoryRepository.findAllByUser(user)
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    // 타입별 카테고리 조회
    public List<CategoryResponse> getCategoriesByType(Long userId, CategoryType type) {
        User user = getUser(userId);
        return categoryRepository.findAllByUserAndType(user, type)
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    // 카테고리 생성
    @Transactional // 메서드 - 쓰기용
    public CategoryResponse createCategory(Long userId, CategoryRequest request) {
        User user = getUser(userId);

        if (categoryRepository.existsByUserAndName(user, request.getName())) {
            throw BusinessException.badRequest("이미 존재하는 카테고리 이름입니다");
        }

        Category category = Category.builder()
                .user(user)
                .name(request.getName())
                .type(request.getType())
                .color(request.getColor())
                .isDefault(false)
                .build();

        return CategoryResponse.from(categoryRepository.save(category));
    }

    // 카테고리 수정
    @Transactional // 메서드 - 쓰기용(JPA의 자동 변경감지) 엔티티 필드를 바꾸면 트랜잭션이 끝날 때 JPA가 알아서 UPDATE 쿼리를 날린다.
    // save()를 따로안해도됨
    public CategoryResponse updateCategory(Long userId, Long categoryId,
                                           CategoryRequest request) {
        User user = getUser(userId);
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> BusinessException.notFound("카테고리를 찾을 수 없습니다"));

        if (category.isDefault()) {
            throw BusinessException.badRequest("기본 카테고리는 수정할 수 없습니다");
        }

        // 이름 중복 체크 (본인 카테고리 제외)
        if (!category.getName().equals(request.getName()) &&
                categoryRepository.existsByUserAndName(user, request.getName())) {
            throw BusinessException.badRequest("이미 존재하는 카테고리 이름입니다");
        }

        category.update(request.getName(), request.getColor());
        return CategoryResponse.from(category);
    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        User user = getUser(userId);
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> BusinessException.notFound("카테고리를 찾을 수 없습니다"));

        if (category.isDefault()) {
            throw BusinessException.badRequest("기본 카테고리는 삭제할 수 없습니다");
        }

        categoryRepository.delete(category);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> BusinessException.notFound("유저를 찾을 수 없습니다"));
    }
}