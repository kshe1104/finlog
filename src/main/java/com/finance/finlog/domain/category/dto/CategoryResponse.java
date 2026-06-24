package com.finance.finlog.domain.category.dto;

import com.finance.finlog.domain.category.entity.Category;
import com.finance.finlog.domain.category.entity.CategoryType;
import lombok.Getter;

@Getter
public class CategoryResponse {
    private final Long id;
    private final String name;
    private final CategoryType type;
    private final String color;
    private final boolean isDefault;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.type = category.getType();
        this.color = category.getColor();
        this.isDefault = category.isDefault();
    }

    // 정적 팩토리 메서드로 생성자 호출을 대신함
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category);
    }
}
