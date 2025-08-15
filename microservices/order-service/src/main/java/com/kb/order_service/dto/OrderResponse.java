package com.kb.order_service.dto;

import com.kb.order_service.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private Long id;
    private Long userId;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderItemResponse> orderItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 