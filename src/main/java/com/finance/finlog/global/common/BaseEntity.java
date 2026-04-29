package com.finance.finlog.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // auditing(감시자) 활성화 엔티티가 생성되거나 수정될 때 자동으로 시간을 기록한다
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false) // 이후 수정해도 이 시간은 바뀌지 않음
    private LocalDate createdAt;

    @LastModifiedDate // 수정될 때마다 현재시간으로 업데이트
    private LocalDate updatedAt;
}
