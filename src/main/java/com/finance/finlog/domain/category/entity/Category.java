package com.finance.finlog.domain.category.entity;

import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor

public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩방식으로 N+1문제 방지함
    @JoinColumn(name = "user_id", nullable = false) // 외래키 컬럼이름을 명시적으로 정하면 DB컬럼명이 예측가능해서 유지보수에 유리하다.
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type;

    private String color;

    @Column(nullable = false)
    private boolean isDefault; // 기본 카테고리와 유저가 직접 만든 카테고리 구분
    //isDefault가 true인 카테고리는 삭제하지 못하도록
}
