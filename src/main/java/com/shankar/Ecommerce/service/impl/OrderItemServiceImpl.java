package com.shankar.Ecommerce.service.impl;

import com.shankar.Ecommerce.dtos.*;
import com.shankar.Ecommerce.entites.Order;
import com.shankar.Ecommerce.entites.OrderItem;
import com.shankar.Ecommerce.entites.Product;
import com.shankar.Ecommerce.entites.User;
import com.shankar.Ecommerce.enums.OrderStatus;
import com.shankar.Ecommerce.exception.NotFoundException;
import com.shankar.Ecommerce.repository.OrderItemRepository;
import com.shankar.Ecommerce.repository.OrderRepository;
import com.shankar.Ecommerce.repository.ProductRepository;
import com.shankar.Ecommerce.service.OrderItemService;
import com.shankar.Ecommerce.service.UserService;

import com.shankar.Ecommerce.sprcification.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse<OrderDTO> placeOrder(OrderRequest orderRequest) {

        User user = userService.getLoginUser();

        log.info("Place order request | User Id: {}", user.getId());

        List<OrderItem> orderItems = orderRequest.getItems().stream()
                .map(orderItemRequest -> {

                    Product product = productRepository.findById(orderItemRequest.getProductId())
                            .orElseThrow(() -> {
                                log.warn("Product not found | Product Id: {}", orderItemRequest.getProductId());
                                return new NotFoundException(
                                        "Product not found with id: " + orderItemRequest.getProductId());
                            });

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemRequest.getQuantity());
                    orderItem.setPrice(
                            product.getPrice()
                                    .multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()))
                    );
                    orderItem.setStatus(OrderStatus.PENDING);
                    orderItem.setUser(user);

                    return orderItem;
                })
                .toList();

        log.info("Successfully created {} order items", orderItems.size());

        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        orderItems.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        log.info("Order placed successfully | Order Id: {} | Total Price: {}",
                savedOrder.getId(), savedOrder.getTotalPrice());

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

        return ApiResponse.<OrderDTO>builder()
                .status(201)
                .message("Order placed successfully")
                .data(orderDTO)
                .build();
    }

    @Override
    public ApiResponse<OrderItemDTO> updateOrderItemStatus(
            Long orderItemId,
            OrderStatus status) {

        log.info("Update order item status request | OrderItem Id: {} | Status: {}",
                orderItemId, status);

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> {
                    log.warn("OrderItem not found | Id: {}", orderItemId);
                    return new NotFoundException(
                            "Order Item not found with id: " + orderItemId);
                });

        orderItem.setStatus(status);

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);

        log.info("Order item status updated successfully | OrderItem Id: {}",
                updatedOrderItem.getId());

        OrderItemDTO response = modelMapper.map(updatedOrderItem, OrderItemDTO.class);

        return ApiResponse.<OrderItemDTO>builder()
                .status(200)
                .message("Order item status updated successfully")
                .data(response)
                .build();
    }


    @Override
    public ApiResponse<Page<OrderItemDTO>> filterOrderItems(
            OrderStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long itemId,
            Pageable pageable) {

        log.info("Filter order items request | Status: {} | StartDate: {} | EndDate: {} | ItemId: {}",
                status, startDate, endDate, itemId);

        Specification<OrderItem> spec = Specification
                .where(OrderItemSpecification.hasStatus(status))
                .and(OrderItemSpecification.createdBetween(startDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));

        Page<OrderItem> orderItemPage = orderItemRepository.findAll(spec, pageable);

        if (orderItemPage.isEmpty()) {
            log.warn("No order items found for given filter");
            throw new NotFoundException("No order items found");
        }

        Page<OrderItemDTO> response = orderItemPage.map(orderItem ->
                modelMapper.map(orderItem, OrderItemDTO.class));

        log.info("Successfully fetched {} order items",
                response.getTotalElements());

        return ApiResponse.<Page<OrderItemDTO>>builder()
                .status(200)
                .message("Order items retrieved successfully")
                .data(response)
                .build();
    }
}
