package com.khaikin.delivery.service.impl;

import com.khaikin.delivery.dto.tracking.OrderTrackingHistoryResponse;
import com.khaikin.delivery.entity.OrderTrackingHistory;
import com.khaikin.delivery.repository.OrderRepository;
import com.khaikin.delivery.repository.OrderTrackingHistoryRepository;
import com.khaikin.delivery.service.TrackingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrackingServiceImpl implements TrackingService {
    private final OrderRepository orderRepository;
    private final OrderTrackingHistoryRepository historyRepository;

    @Override
    public List<OrderTrackingHistoryResponse> getTrackingHistory(String orderCode) {
        List<OrderTrackingHistory> historyList = historyRepository
                .findByOrderOrderCodeOrderByChangedAtAsc(orderCode);

        return historyList.stream().map(h -> new OrderTrackingHistoryResponse(
                h.getStatus(),
                h.getChangedAt(),
                h.getChangedBy()
        )).collect(Collectors.toList());
    }
}
