package com.finance.finlog.domain.category.service;

import com.finance.finlog.domain.category.dto.CategoryRequest;
import com.finance.finlog.domain.category.dto.CategoryResponse;
import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.category.entity.CategoryType;
import com.finance.finlog.domain.category.repository.CategoryRepository;
import com.finance.finlog.domain.user.entity.Provider;
import com.finance.finlog.domain.user.entity.Role;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.domain.user.repository.UserRepository;
import com.finance.finlog.global.common.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryService categoryService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@gmail.com")
                .name("테스트유저")
                .provider(Provider.GOOGLE)
                .providerId("google-123")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("카테고리 목록을 정상적으로 조회한다")
    void getCategories_success() {
        // given
        Category category1 = Category.builder()
                .user(user).name("식비")
                .type(CategoryType.EXPENSE)
                .isDefault(false).build();
        Category category2 = Category.builder()
                .user(user).name("월급")
                .type(CategoryType.INCOME)
                .isDefault(false).build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(categoryRepository.findAllByUser(user))
                .willReturn(List.of(category1, category2));

        // when
        List<CategoryResponse> result = categoryService.getCategories(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name")
                .containsExactlyInAnyOrder("식비", "월급");
    }

    @Test
    @DisplayName("카테고리 생성에 성공한다")
    void createCategory_success() {
        // given
        CategoryRequest request = new CategoryRequest();
        setField(request, "name", "카페");
        setField(request, "type", CategoryType.EXPENSE);
        setField(request, "color", "#FF0000");

        Category saved = Category.builder()
                .user(user).name("카페")
                .type(CategoryType.EXPENSE)
                .color("#FF0000")
                .isDefault(false).build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(categoryRepository.existsByUserAndName(user, "카페")).willReturn(false);
        given(categoryRepository.save(any(Category.class))).willReturn(saved);

        // when
        CategoryResponse result = categoryService.createCategory(1L, request);

        // then
        assertThat(result.getName()).isEqualTo("카페");
        assertThat(result.getType()).isEqualTo(CategoryType.EXPENSE);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("중복된 카테고리 이름으로 생성하면 예외가 발생한다")
    void createCategory_duplicateName_throwsException() {
        // given
        CategoryRequest request = new CategoryRequest();
        setField(request, "name", "식비");
        setField(request, "type", CategoryType.EXPENSE);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(categoryRepository.existsByUserAndName(user, "식비")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryService.createCategory(1L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("이미 존재하는 카테고리 이름입니다");
    }

    @Test
    @DisplayName("기본 카테고리는 삭제할 수 없다")
    void deleteCategory_defaultCategory_throwsException() {
        // given
        Category defaultCategory = Category.builder()
                .user(user).name("식비")
                .type(CategoryType.EXPENSE)
                .isDefault(true).build(); // 기본 카테고리

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(categoryRepository.findByIdAndUser(1L, user))
                .willReturn(Optional.of(defaultCategory));

        // when & then
        assertThatThrownBy(() -> categoryService.deleteCategory(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("기본 카테고리는 삭제할 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 유저로 조회하면 예외가 발생한다")
    void getCategories_userNotFound_throwsException() {
        // given
        given(userRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> categoryService.getCategories(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("유저를 찾을 수 없습니다");
    }

    // Lombok이 Setter를 막아놔서 리플렉션으로 필드 설정
    private void setField(Object obj, String fieldName, Object value) {
        try {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}