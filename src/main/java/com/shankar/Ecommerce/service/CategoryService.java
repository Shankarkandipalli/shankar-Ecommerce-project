package com.shankar.Ecommerce.service;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.CategoryDTO;

import java.util.List;

public interface CategoryService {

    ApiResponse<CategoryDTO> createCategory(CategoryDTO categoryDTO);

    ApiResponse<CategoryDTO> updateCategory(Long categoryId, CategoryDTO categoryDTO);

    ApiResponse<CategoryDTO> getByCategoryId(Long categoryId);

    ApiResponse<?> deleteCategory(Long categoryId);

    ApiResponse<List<CategoryDTO>> getAllCategory();


}
