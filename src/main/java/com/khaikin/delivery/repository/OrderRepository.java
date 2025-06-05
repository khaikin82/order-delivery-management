package com.khaikin.delivery.repository;

import com.khaikin.delivery.entity.Order;
import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByCustomer(User customer);
    List<Order> findByDeliveryStaff(User staff);
    List<Order> findByStatus(OrderStatus status);
}
