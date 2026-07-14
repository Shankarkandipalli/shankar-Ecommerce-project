package com.shankar.Ecommerce.dtos;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long productId;
    private int quantity;


}
