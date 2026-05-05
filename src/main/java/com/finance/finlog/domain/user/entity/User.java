package com.finance.finlog.domain.user.entity;

import com.finance.finlog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자를 public으로 열어두면 new User()를 통해 아무데서나 빈 객체를 만들 수 있어서 위험하다. Protected로 만들면 jpa내부에서만 사용가능.

@Builder // Builder + AllArgsConstructor는 세트
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING) // EnumType.Original로 받으면 나중에 순서가 꼬임
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // OAuth 로그인 시 이름/프로필 이미지 업데이트 메서드
    // Setter 대신 메서드로 값을 변경
    public void updateProfile(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
