package com.bookstore.service;

import com.bookstore.domain.Book;
import com.bookstore.domain.Order;
import com.bookstore.domain.OrderItem;
import com.bookstore.domain.enums.OrderStatus;
import com.bookstore.dto.request.OrderItemRequest;
import com.bookstore.dto.request.PlaceOrderRequest;
import com.bookstore.dto.request.UpdateOrderStatusRequest;
import com.bookstore.dto.response.OrderResponse;
import com.bookstore.dto.response.PageResponse;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.InsufficientInventoryException;
import com.bookstore.exception.InvalidOrderStateException;
import com.bookstore.exception.OrderNotFoundException;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        List<OrderItemRequest> itemRequests = request.getItems();

        // Validate all books exist and have sufficient stock, then build order items — single DB fetch per book
        List<OrderItem> orderItems = itemRequests.stream()
                .map(itemRequest -> {
                    Book book = bookRepository.findByIsbn(itemRequest.getBookIsbn())
                            .orElseThrow(() -> new BookNotFoundException(itemRequest.getBookIsbn()));

                    if (!book.hasStock(itemRequest.getQuantity())) {
                        throw new InsufficientInventoryException(
                                book.getIsbn(), itemRequest.getQuantity(), book.getStockQuantity());
                    }

                    OrderItem item = orderMapper.toEntity(itemRequest);
                    item.setBook(book);
                    item.setUnitPrice(book.getPrice()); // Snapshot price at order time
                    return item;
                })
                .collect(Collectors.toList());

        // Create order
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .status(OrderStatus.PENDING)
                .placedAt(LocalDateTime.now())
                .build();

        orderItems.forEach(order::addItem);

        // Calculate total and set
        BigDecimal total = order.calculateTotal();
        order.setTotalAmount(total);

        // Decrease stock for each book
        orderItems.forEach(item -> item.getBook().decreaseStock(item.getQuantity()));

        // Save order (cascades to order items)
        Order saved = orderRepository.save(order);

        return orderMapper.toResponse(saved);
    }

    public OrderResponse getOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
        return orderMapper.toResponse(order);
    }

    public PageResponse<OrderResponse> getOrders(OrderStatus status, Pageable pageable) {
        Page<Order> orders = (status != null)
                ? orderRepository.findByStatus(status, pageable)
                : orderRepository.findAll(pageable);
        return PageResponse.from(orders.map(orderMapper::toResponse));
    }

    @Transactional
    public OrderResponse updateOrderStatus(String orderNumber, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));

        OrderStatus newStatus = request.getStatus();
        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);

        // If cancelling, restore inventory
        if (newStatus == OrderStatus.CANCELLED) {
            restoreInventory(order);
        }

        return orderMapper.toResponse(saved);
    }

    @Transactional
    public void cancelOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));

        if (!order.canBeCancelled()) {
            throw new InvalidOrderStateException(orderNumber, order.getStatus(), "cancel");
        }

        order.setStatus(OrderStatus.CANCELLED);
        restoreInventory(order);
        orderRepository.save(order);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        boolean valid = false;
        switch (current) {
            case PENDING:
                valid = next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
                break;
            case CONFIRMED:
                valid = next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
                break;
            case SHIPPED:
                valid = next == OrderStatus.DELIVERED;
                break;
            case DELIVERED:
            case CANCELLED:
                valid = false; // Terminal states
                break;
        }

        if (!valid) {
            throw new InvalidOrderStateException(
                    "Cannot transition from " + current + " to " + next);
        }
    }

    private void restoreInventory(Order order) {
        order.getItems().forEach(item -> item.getBook().increaseStock(item.getQuantity()));
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}