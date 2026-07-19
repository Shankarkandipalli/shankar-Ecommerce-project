package com.shankar.Ecommerce.service;

import com.shankar.Ecommerce.dtos.*;

import com.shankar.Ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderItemService {

    ApiResponse<OrderDTO> placeOrder(OrderRequest orderRequest);

    ApiResponse<OrderItemDTO> updateOrderItemStatus(Long orderItemId, OrderStatus status);

    ApiResponse<Page<OrderItemDTO>> filterOrderItems(
            OrderStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long itemId,
            Pageable pageable);
}
