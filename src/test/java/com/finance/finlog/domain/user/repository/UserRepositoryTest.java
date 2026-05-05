package com.finance.finlog.domain.user.repository;

import com.finance.finlog.domain.user.entity.Provider;
import com.finance.finlog.domain.user.entity.Role;
import com.finance.finlog.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat; // AssertJ 사용 시


// JPA 관련 빈만 로딩 repository, entityManager만 뜬다.
// H2 인메모리 DB 자동사용
// SpringBootTest보다 가볍고 빠름
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Provider와 ProviderId로 유저를 조회할 수 있다")
    void findByProviderAndProviderId_Success() {
        //given
        User user = User.builder()
                .email("test@gmail.com")
                .name("테스트유저")
                .provider(Provider.GOOGLE)
                .providerId("google-123")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        //when
        Optional<User> result = userRepository.findByProviderAndProviderId(Provider.GOOGLE, "google-123");

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@gmail.com");
        assertThat(result.get().getProvider()).isEqualTo(Provider.GOOGLE);
    }

    @Test
    @DisplayName("존재하지 않는 ProviderId로 조회하면 빈 Optional을 반환한다")
    void findByProviderAndProviderId_notFound() {
        // given - 아무것도 저장하지 않음

        // when
        Optional<User> result = userRepository.findByProviderAndProviderId(Provider.GOOGLE, "not-exist-id");

        // then
        assertThat(result).isEmpty();
    }


    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("이메일 중복 확인 - 존재하는 이메일이면 true 반환")
    void existsByEmail_true(){
        // given
        User user = User.builder()
                .email("test@gmail.com")
                .name("테스트 유저")
                .provider(Provider.GOOGLE)
                .providerId("google-123")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        entityManager.flush();


        // when
        boolean result = userRepository.existsByEmail("test@gmail.com");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 확인 - 존재하지 않는 이메일이면 false를 반환한다")
    void existsByEmail_false() {
        // given - 아무것도 저장 안 함

        // when
        boolean result = userRepository.existsByEmail("notexist@gmail.com");

        // then
        assertThat(result).isFalse();
    }
}
