package com.khaikin.delivery.repository;

import com.khaikin.delivery.entity.Order;
import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByOrderCode(String orderCode);
    Page<Order> findByCustomer(User customer, Pageable pageable);
    List<Order> findByDeliveryStaff(User staff);
    Page<Order> findByDeliveryStaff(User staff, Pageable pageable);
    List<Order> findByStatus(OrderStatus status);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
