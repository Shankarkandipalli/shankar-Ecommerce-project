package com.shankar.Ecommerce.controller;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.ProductDTO;
import com.shankar.Ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @PathVariable Long categoryId,
            @RequestBody ProductDTO productDTO) {

        ApiResponse<ProductDTO> addProducts = productService.createProduct(categoryId, productDTO);
        return new ResponseEntity<>(addProducts, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long productId,
            @RequestParam Long categoryId,
            @RequestBody ProductDTO productDTO) {

        return ResponseEntity.ok(
                productService.updateProduct(productId, categoryId, productDTO)
        );
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(
                productService.deleteProduct(productId)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getByProductId(productId));
    }

    @GetMapping("/get-by-category-id/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                productService.getProductsByCategory(categoryId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchForProduct(
            @RequestParam String searchValue) {
        return ResponseEntity.ok(productService.searchProduct(searchValue));
    }

}

