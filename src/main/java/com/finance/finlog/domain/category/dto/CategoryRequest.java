package com.finance.finlog.domain.category.dto;

import com.finance.finlog.domain.category.entity.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CategoryRequest {
    @NotBlank(message = "카테고리 이름을 입력하세요")
    private String name;

    @NotNull(message = "카테고리 타입을 입력하세요")
    private CategoryType type;

    private String color;
}
