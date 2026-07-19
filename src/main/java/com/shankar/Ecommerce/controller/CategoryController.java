package com.shankar.Ecommerce.controller;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.CategoryDTO;
import com.shankar.Ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryDTO>> addCategory(@RequestBody CategoryDTO categoryDTO) {
        ApiResponse<CategoryDTO> addCategorys = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(addCategorys, HttpStatus.CREATED);

    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        ApiResponse<CategoryDTO> addCategorys = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(addCategorys, HttpStatus.OK);

    }

    @DeleteMapping("/{categoryId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable Long categoryId) {
        ApiResponse<?> addCategorys = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(addCategorys, HttpStatus.OK);

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getByCategoryId(@PathVariable Long categoryId) {
        ApiResponse<CategoryDTO> getCategoryId = categoryService.getByCategoryId(categoryId);
        return new ResponseEntity<>(getCategoryId, HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());

    }


}
