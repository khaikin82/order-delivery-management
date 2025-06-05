package com.khaikin.delivery.repository;

import com.khaikin.delivery.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    List<OrderStatusHistory> findByOrderOrderCodeOrderByUpdatedAtAsc(String orderCode);
}

