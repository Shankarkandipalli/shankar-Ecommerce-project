package com.shankar.Ecommerce.service.impl;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.CategoryDTO;
import com.shankar.Ecommerce.entites.Category;
import com.shankar.Ecommerce.exception.BadRequestException;
import com.shankar.Ecommerce.exception.NotFoundException;
import com.shankar.Ecommerce.repository.CategoryRepository;
import com.shankar.Ecommerce.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;


import java.util.List;


@AllArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<CategoryDTO> createCategory(CategoryDTO categoryDTO) {

        log.info("Create category request | Name: {}", categoryDTO.getName());

        if (categoryRepository.existsByName(categoryDTO.getName())) {
            log.warn("Category already exists | Name: {}", categoryDTO.getName());
            throw new BadRequestException("Category already exists");
        }
        Category category = modelMapper.map(categoryDTO, Category.class);

        Category savedCategory = categoryRepository.save(category);

        log.info("Category created successfully | Id: {}", savedCategory.getId());

        CategoryDTO response = modelMapper.map(savedCategory, CategoryDTO.class);

        return ApiResponse.<CategoryDTO>builder()
                .status(201)
                .message("Category Added Successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<CategoryDTO> updateCategory(Long categoryId, CategoryDTO categoryDTO) {

        log.info("Update category request | Id: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("categoryId not found | Id: {}", categoryId);
                    return new NotFoundException("Category not found with id: " + categoryId);
                });

        if (categoryRepository.existsByName(categoryDTO.getName())
                && !category.getName().equalsIgnoreCase(categoryDTO.getName())) {

            log.warn("Category already exists | Name: {}", categoryDTO.getName());
            throw new BadRequestException("Category already exists");
        }

        category.setName(categoryDTO.getName());

        Category updatedCategory = categoryRepository.save(category);

        log.info("Category updated successfully | Id: {}", updatedCategory.getId());

        CategoryDTO response = modelMapper.map(updatedCategory, CategoryDTO.class);

        return ApiResponse.<CategoryDTO>builder()
                .status(200)
                .message("Category Updated Successfully")
                .data(response)
                .build();
    }


    @Override
    public ApiResponse<CategoryDTO> getByCategoryId(Long categoryId) {

        log.info("Fetching category | Id: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found | Id: {}", categoryId);
                    return new NotFoundException("Category not found with id: " + categoryId);
                });

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        log.info("Category retrieved successfully | Id: {}", categoryId);

        return ApiResponse.<CategoryDTO>builder()
                .status(200)
                .message("Category Retrieved Successfully")
                .data(categoryDTO)
                .build();
    }

    @Override
    public ApiResponse<?> deleteCategory(Long categoryId) {

        log.info("Delete category request | Id: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found | Id: {}", categoryId);
                    return new NotFoundException("Category not found with id: " + categoryId);
                });

        categoryRepository.delete(category);

        log.info("Category deleted successfully | Id: {}", categoryId);

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Category deleted successfully")
                .build();
    }

    @Override
    public ApiResponse<List<CategoryDTO>> getAllCategory() {

        log.info("Fetching all categories");

        List<Category> categories = categoryRepository.findAll();

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        log.info("Successfully fetched {} categories", categoryDTOs.size());

        return ApiResponse.<List<CategoryDTO>>builder()
                .status(200)
                .message("Categories retrieved successfully")
                .data(categoryDTOs)
                .build();
    }
}
