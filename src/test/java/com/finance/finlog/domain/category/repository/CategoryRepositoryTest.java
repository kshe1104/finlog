package com.finance.finlog.domain.category.repository;

import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.category.entity.CategoryType;
import com.finance.finlog.domain.user.entity.Provider;
import com.finance.finlog.domain.user.entity.Role;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat; // AssertJ 사용 시


@DataJpaTest // 테스트임을 명시 -> Transaction이 끝나도 DB에 영향 x
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

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

        userRepository.save(user);
    }

    @Test
    @DisplayName("유저의 전체 카테고리를 조회할 수 있다.")
    void findallByUser_success() {
        // given
        Category category1 = Category.builder()
                .user(user)
                .name("식비")
                .type(CategoryType.EXPENSE)
                .color("#FF0000")
                .isDefault(true)
                .build();

        Category category2 = Category.builder()
                .user(user)
                .name("월급")
                .type(CategoryType.EXPENSE)
                .color("#0000FF")
                .isDefault(true)
                .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        // when
        List<Category> result = categoryRepository.findAllByUser(user);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name")
                .containsExactlyInAnyOrder("식비", "월급");
    }

    @Test
    @DisplayName("카테고리 타입으로 필터링해서 조회할 수 있다")
    void findAllByUserAndType_success(){
        // given
        Category expense = Category.builder()
                .user(user).name("식비")
                .type(CategoryType.EXPENSE)
                .isDefault(true).build();

        Category income = Category.builder()
                .user(user).name("월급")
                .type(CategoryType.INCOME)
                        .isDefault(true).build();

        categoryRepository.save(expense);
        categoryRepository.save(income);

        // when
        List<Category> result = categoryRepository.findAllByUserAndType(user, CategoryType.EXPENSE);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("식비");
        assertThat(result.get(0).getType()).isEqualTo(CategoryType.EXPENSE);
    }

    @Test
    @DisplayName("다른 유저의 카테고리는 조회되지 않는다")
    void findByIdAndUser_otherUserCategory_notFound() {
        // given
        User otherUser = User.builder()
                .email("other@gmail.com")
                .name("다른유저")
                .provider(Provider.GOOGLE)
                .providerId("google-456")
                .role(Role.USER)
                .build();
        userRepository.save(otherUser);

        Category category = Category.builder()
                .user(otherUser)
                .name("식비")
                .type(CategoryType.EXPENSE)
                .isDefault(true)
                .build();
        categoryRepository.save(category);

        // when - 다른 유저의 카테고리를 내 user로 조회 시도
        Optional<Category> result = categoryRepository
                .findByIdAndUser(category.getId(), user);

        // then
        assertThat(result).isEmpty(); // 조회 안 됨
    }

    @Test
    @DisplayName("같은 유저에게 중복 카테고리 이름이 있으면 true를 반환한다")
    void existsByUserAndName_true() {
        // given
        Category category = Category.builder()
                .user(user).name("식비")
                .type(CategoryType.EXPENSE)
                .isDefault(true).build();
        categoryRepository.save(category);

        // when
        boolean result = categoryRepository.existsByUserAndName(user, "식비");

        // then
        assertThat(result).isTrue();
    }
}
