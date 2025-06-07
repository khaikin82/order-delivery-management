package com.khaikin.delivery.listener;

import com.khaikin.delivery.entity.OrderTrackingHistory;
import com.khaikin.delivery.event.OrderStatusChangedEvent;
import com.khaikin.delivery.repository.OrderRepository;
import com.khaikin.delivery.repository.OrderTrackingHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusChangedListener {

    private final OrderTrackingHistoryRepository trackingHistoryRepository;
    private final OrderRepository orderRepository;

    @EventListener
    public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("OrderStatusChangedEvent received: OrderId={}, {} -> {}, by={}",
                 event.getOrderId(), event.getOldStatus(), event.getNewStatus(), event.getPerformedBy());

        // Lấy order entity (optional nếu cần để liên kết)
        var order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found in listener"));

        // Tạo lịch sử tracking mới
        OrderTrackingHistory history = new OrderTrackingHistory();
        history.setOrder(order);
        history.setStatus(event.getNewStatus());
        history.setChangedAt(LocalDateTime.now());
        history.setChangedBy(event.getPerformedBy());

        trackingHistoryRepository.save(history);
    }
}