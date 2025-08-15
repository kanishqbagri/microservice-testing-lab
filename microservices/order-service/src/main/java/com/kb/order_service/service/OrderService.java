package com.kb.order_service.service;

import com.kb.order_service.dto.OrderRequest;
import com.kb.order_service.dto.OrderResponse;
import com.kb.order_service.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    
    OrderResponse createOrder(OrderRequest orderRequest);
    
    OrderResponse getOrderById(Long id);
    
    OrderResponse getOrderByOrderNumber(String orderNumber);
    
    List<OrderResponse> getOrdersByUserId(Long userId);
    
    List<OrderResponse> getOrdersByStatus(OrderStatus status);
    
    List<OrderResponse> getAllOrders();
    
    OrderResponse updateOrderStatus(Long id, OrderStatus status);
    
    void deleteOrder(Long id);
} 