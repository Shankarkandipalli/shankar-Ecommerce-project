package com.shankar.Ecommerce.service;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.ProductDTO;

import java.util.List;

public interface ProductService {

    ApiResponse<ProductDTO>createProduct(Long categoryId,ProductDTO productDTO);
    ApiResponse<ProductDTO>updateProduct(Long productId,Long categoryId,ProductDTO productDTO);
    ApiResponse<?>deleteProduct(Long productId);
    ApiResponse<List<ProductDTO>> getAllProduct();
    ApiResponse<ProductDTO>getByProductId(Long productId);
    ApiResponse<List<ProductDTO>> getProductsByCategory(Long categoryId);
    ApiResponse<List<ProductDTO>> searchProduct(String searchValue);


}
