package com.finance.finlog.domain.category.controller;

import com.finance.finlog.domain.category.dto.CategoryRequest;
import com.finance.finlog.domain.category.dto.CategoryResponse;
import com.finance.finlog.domain.category.entity.CategoryType;
import com.finance.finlog.domain.category.service.CategoryService;
import com.finance.finlog.global.common.CommonResponse;
import com.finance.finlog.global.common.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "카테고리", description = "카테고리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    // (required=false) -> 필터 조건은 선택사항이다. 라는 옵션
    // 기본값은 true인데, 그러면 Get /api/categories?type=EXPENSE 이렇게 조건을 무조건 요청해야함(아니면 에러뜸)
    @Operation(summary = "카테고리 목록 조회", description = "전체 또는 타입별 카테고리 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<List<CategoryResponse>>> getCategories(@CurrentUser Long userId, @RequestParam(required = false) CategoryType type) {
        List<CategoryResponse> result = type != null ? categoryService.getCategoriesByType(userId, type) : categoryService.getCategories(userId);

        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @Operation(summary = "카테고리 생성")
    @PostMapping
    public ResponseEntity<CommonResponse<CategoryResponse>> createCategory(@CurrentUser Long userId, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.createCategory(userId, request);
        return ResponseEntity.ok(CommonResponse.success(result, "카테고리가 등록되었습니다."));
    }

    @Operation(summary = "카테고리 수정")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CommonResponse<CategoryResponse>> updateCategory(@CurrentUser Long userId, @PathVariable Long categoryId, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.updateCategory(userId, categoryId, request);

        return ResponseEntity.ok(CommonResponse.success(result, "카테고리가 수정됐습니다"));
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CommonResponse<Void>> deleteCategory(@CurrentUser Long userId, @PathVariable Long categoryId) {

        categoryService.deleteCategory(userId, categoryId);
        return ResponseEntity.ok(CommonResponse.success(null, "카테고리가 삭제됐습니다"));
    }
}
