package com.bookstore.repository;

import com.bookstore.domain.Order;
import com.bookstore.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.placedAt BETWEEN :start AND :end ORDER BY o.placedAt DESC")
    Page<Order> findByPlacedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    List<Order> findByStatusIn(List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.placedAt < :threshold")
    List<Order> findStaleOrders(@Param("status") OrderStatus status, @Param("threshold") LocalDateTime threshold);

    boolean existsByOrderNumber(String orderNumber);

    long countByStatus(OrderStatus status);
}