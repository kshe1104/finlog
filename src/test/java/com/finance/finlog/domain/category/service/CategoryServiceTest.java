package com.finance.finlog.domain.category.service;

import com.finance.finlog.domain.category.dto.CategoryResponse;
import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.category.entity.CategoryType;
import com.finance.finlog.domain.category.repository.CategoryRepository;
import com.finance.finlog.domain.user.entity.Provider;
import com.finance.finlog.domain.user.entity.Role;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.domain.user.repository.UserRepository;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryService categoryService;

    private User user;

    @BeforeEach
    void setUp()
    {
        user = User.builder()
                .email("test@gmail.com")
                .name("테스트 유저")
                .provider(Provider.GOOGLE)
                .providerId("google-123")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("카테고리 목록을 정상 조회한다.")
    void getCategories_success(){
        // given
        Category category1 = Category.builder()
                .user(user).name("식비")
                .type(CategoryType.EXPENSE)
                .isDefault(false).build();

        Category category2 = Category.builder()
                .user(user).name("식비")
                .type(CategoryType.INCOME)
                .isDefault(false).build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(categoryRepository.findAllByUser(user))
                .willReturn(List.of(category1, category2));

        // when
        List<CategoryResponse> result = categoryService.getCategories(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name").containsExactlyInAnyOrder("식비", "월급");
    }
}
