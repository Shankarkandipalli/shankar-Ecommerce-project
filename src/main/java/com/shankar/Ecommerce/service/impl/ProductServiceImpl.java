package com.shankar.Ecommerce.service.impl;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.ProductDTO;
import com.shankar.Ecommerce.entites.Category;
import com.shankar.Ecommerce.entites.Product;
import com.shankar.Ecommerce.exception.NotFoundException;
import com.shankar.Ecommerce.repository.CategoryRepository;
import com.shankar.Ecommerce.repository.ProductRepository;
import com.shankar.Ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<ProductDTO> createProduct(Long categoryId, ProductDTO productDTO) {

        log.info("Create product request | CategoryId: {} | Product: {}",
                categoryId, productDTO.getName());
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found | Id: {}", categoryId);
                    return new NotFoundException("Category not found with id: " + categoryId);
                });
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully | Id: {}", savedProduct.getId());
        ProductDTO response = modelMapper.map(savedProduct, ProductDTO.class);
        return ApiResponse.<ProductDTO>builder()
                .status(201)
                .message("Product created successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<ProductDTO> updateProduct(Long productId, Long categoryId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("productId Id not found | Id: {}", productId);
                    return new NotFoundException("productId not found with id: " + productId);
                });

        log.info("update product request | CategoryId: {} | Product: {}",
                categoryId, productDTO.getName());

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category Id not found | Id: {}", categoryId);
                    return new NotFoundException("Category not found with id: " + categoryId);
                });
        product.setCategory(category);
        if (productDTO.getName() != null) ;
        {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) ;
        {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getName() != null) ;
        {
            product.setImageUrl(productDTO.getImageUrl());
        }
        if (productDTO.getName() != null) ;
        {
            product.setPrice(productDTO.getPrice());
        }
        Product savedProduct = productRepository.save(product);
        log.info("Product updated successfully | Id: {}", savedProduct.getId());
        ProductDTO savedProductDtos = modelMapper.map(savedProduct, ProductDTO.class);

        return ApiResponse.<ProductDTO>builder()
                .status(201)
                .message("Product updated successfully")
                .data(savedProductDtos)
                .build();


    }

    @Override
    public ApiResponse<?> deleteProduct(Long productId) {
        log.info("Delete product request | Id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found | Id: {}", productId);
                    return new NotFoundException("Product not found with id: " + productId);
                });
        productRepository.delete(product);
        log.info("Product deleted successfully | Id: {}", productId);
        return ApiResponse.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    //Sort.by(Sort.Direction.DESC, "id")
    @Override
    public ApiResponse<List<ProductDTO>> getAllProduct() {

        log.info("Fetching all products");

        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        log.info("Successfully fetched {} products", productDTOs.size());

        return ApiResponse.<List<ProductDTO>>builder()
                .status(200)
                .message("Products retrieved successfully")
                .data(productDTOs)
                .build();
    }

    @Override
    public ApiResponse<ProductDTO> getByProductId(Long productId) {

        log.info("Fetching product | Id: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Products not found | Id: {}", productId);
                    return new NotFoundException("Product not found with id: " + productId);
                });

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        log.info("Product retrieved successfully | Id: {}", productId);

        return ApiResponse.<ProductDTO>builder()
                .status(200)
                .message("Product retrieved successfully")
                .data(productDTO)
                .build();
    }

    @Override
    public ApiResponse<List<ProductDTO>> getProductsByCategory(Long categoryId) {

        log.info("Fetching products | CategoryId: {}", categoryId);

        List<Product> products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            log.warn("No products found | CategoryId: {}", categoryId);
            throw new NotFoundException("No products found for category id: " + categoryId);
        }

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        log.info("Successfully fetched {} products for category {}", productDTOs.size(), categoryId);

        return ApiResponse.<List<ProductDTO>>builder()
                .status(200)
                .message("Products retrieved successfully")
                .data(productDTOs)
                .build();
    }

    @Override
    public ApiResponse<List<ProductDTO>> searchProduct(String searchValue) {
        List<Product> products = productRepository.findByNameContainingOrDescriptionContaining(searchValue, searchValue);

        if (products.isEmpty()) {
            throw new NotFoundException("No Products Found");
        }
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return ApiResponse.<List<ProductDTO>>builder()
                .status(200)
                .message("Products retrieved successfully")
                .data(productDTOs)
                .build();
    }
}
