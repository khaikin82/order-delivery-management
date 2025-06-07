package com.khaikin.delivery.entity;

import com.khaikin.delivery.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_tracking_history")
public class OrderTrackingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime changedAt;

    private String changedBy;

}