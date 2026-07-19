package com.shankar.Ecommerce.controller;

import com.shankar.Ecommerce.dtos.ApiResponse;
import com.shankar.Ecommerce.dtos.OrderDTO;
import com.shankar.Ecommerce.dtos.OrderItemDTO;
import com.shankar.Ecommerce.dtos.OrderRequest;
import com.shankar.Ecommerce.enums.OrderStatus;
import com.shankar.Ecommerce.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderDTO>> placeOrders(
            @RequestBody OrderRequest orderRequest) {
        ApiResponse<OrderDTO> response = orderItemService.placeOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/updateitemstatus/{orderItemId}")
    public ResponseEntity<ApiResponse<OrderItemDTO>> updateOrderItemStatus(
            @PathVariable Long orderItemId,
            @RequestParam OrderStatus status) {

        ApiResponse<OrderItemDTO> response =
                orderItemService.updateOrderItemStatus(orderItemId, status);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<OrderItemDTO>>> filterOrderItems(

            @RequestParam(required = false) OrderStatus status,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,

            @RequestParam(required = false) Long itemId,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        return ResponseEntity.ok(
                orderItemService.filterOrderItems(
                        status,
                        startDate,
                        endDate,
                        itemId,
                        pageable
                )
        );
    }


}
