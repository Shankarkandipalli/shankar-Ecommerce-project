package com.shankar.Ecommerce.dtos;

import com.shankar.Ecommerce.entites.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {

    private BigDecimal totalPrice;
    private List<OrderItemRequest> items;
    private Payment paymentInfo;

}
