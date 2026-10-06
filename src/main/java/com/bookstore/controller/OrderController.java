package com.bookstore.controller;

import com.bookstore.domain.enums.OrderStatus;
import com.bookstore.dto.request.PlaceOrderRequest;
import com.bookstore.dto.request.UpdateOrderStatusRequest;
import com.bookstore.dto.response.OrderResponse;
import com.bookstore.dto.response.PageResponse;
import com.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place a new order")
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request) {
        OrderResponse response = orderService.placeOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderNumber}")
    @Operation(summary = "Get order by order number")
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "Order number") @PathVariable String orderNumber) {
        OrderResponse response = orderService.getOrder(orderNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get orders with optional status filter and pagination")
    public ResponseEntity<PageResponse<OrderResponse>> getOrders(
            @Parameter(description = "Filter by order status")
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 20, sort = "placedAt") Pageable pageable) {
        PageResponse<OrderResponse> response = orderService.getOrders(status, pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderNumber}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "Order number") @PathVariable String orderNumber,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(orderNumber, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderNumber}")
    @Operation(summary = "Cancel an order (only PENDING or CONFIRMED orders)")
    public ResponseEntity<Void> cancelOrder(
            @Parameter(description = "Order number") @PathVariable String orderNumber) {
        orderService.cancelOrder(orderNumber);
        return ResponseEntity.noContent().build();
    }
}