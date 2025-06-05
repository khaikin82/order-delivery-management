package com.khaikin.delivery.service.impl;

import com.khaikin.delivery.dto.OrderStatusHistoryDto;
import com.khaikin.delivery.entity.OrderStatusHistory;
import com.khaikin.delivery.repository.OrderRepository;
import com.khaikin.delivery.repository.OrderStatusHistoryRepository;
import com.khaikin.delivery.service.TrackingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrackingServiceImpl implements TrackingService {
    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;

    @Override
    public List<OrderStatusHistoryDto> getTrackingHistory(String orderCode) {
        List<OrderStatusHistory> historyList = historyRepository
                .findByOrderOrderCodeOrderByUpdatedAtAsc(orderCode);

        return historyList.stream().map(h -> new OrderStatusHistoryDto(
                h.getStatus(),
                h.getUpdatedAt(),
                h.getUpdatedBy()
        )).collect(Collectors.toList());
    }
}
