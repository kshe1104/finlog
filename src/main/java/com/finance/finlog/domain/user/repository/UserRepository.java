package com.finance.finlog.domain.user.repository;

import com.finance.finlog.domain.user.entity.Provider;
import com.finance.finlog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

// OAuth로 로그인 시 이 소셜 서비스의 이 유저가 우리 DB에 있는가를 확인함
Optional<User> findByProviderAndProviderId(Provider provider, String ProviderId);
// 있으면 -> 로그인 처리
// 없으면 -> 자동 회원가입 후 로그인

    // 이메일로 유저 조회
    Optional<User> findByEmail(String email);

    // 이메일 중복 확인
    // 이 메서드를 따로 만들면 findByEmail로 매번 전체 데이터 다 안가져와도됨
    boolean existsByEmail(String email);
    // select count(*) > 0 from users where email = ?

}
