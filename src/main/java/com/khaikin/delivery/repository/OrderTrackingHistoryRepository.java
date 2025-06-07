package com.khaikin.delivery.repository;

import com.khaikin.delivery.entity.OrderTrackingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTrackingHistoryRepository extends JpaRepository<OrderTrackingHistory, Long> {
    List<OrderTrackingHistory> findByOrderOrderCodeOrderByChangedAtAsc(String orderCode);
}

