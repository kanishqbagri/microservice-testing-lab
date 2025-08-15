package com.kb.order_service.service;

import com.kb.order_service.dto.OrderItemRequest;
import com.kb.order_service.dto.OrderRequest;
import com.kb.order_service.dto.OrderResponse;
import com.kb.order_service.entity.Order;
import com.kb.order_service.entity.OrderStatus;
import com.kb.order_service.exception.OrderNotFoundException;
import com.kb.order_service.repository.OrderRepository;
import com.kb.order_service.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private ModelMapper modelMapper;
    private OrderService orderService;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        modelMapper = new ModelMapper();
        orderService = new OrderServiceImpl(orderRepository, modelMapper);
    }

    @Test
    void createOrder_ShouldSucceed_WhenValidRequest() {
        // Given
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest orderRequest = new OrderRequest(1L, "123 Main St", "123 Main St", Arrays.asList(itemRequest));
        
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderNumber("ORD-ABC12345");
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setTotalAmount(BigDecimal.valueOf(20.00));
        
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        OrderResponse response = orderService.createOrder(orderRequest);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("ORD-ABC12345", response.getOrderNumber());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertEquals(BigDecimal.valueOf(20.00), response.getTotalAmount());
        
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenOrderExists() {
        // Given
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderNumber("ORD-ABC12345");
        order.setStatus(OrderStatus.PENDING);
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        OrderResponse response = orderService.getOrderById(orderId);

        // Then
        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals("ORD-ABC12345", response.getOrderNumber());
    }

    @Test
    void getOrderById_ShouldThrowException_WhenOrderNotFound() {
        // Given
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void updateOrderStatus_ShouldSucceed_WhenOrderExists() {
        // Given
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.CONFIRMED;
        
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.PENDING);
        
        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setStatus(newStatus);
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // When
        OrderResponse response = orderService.updateOrderStatus(orderId, newStatus);

        // Then
        assertNotNull(response);
        assertEquals(newStatus, response.getStatus());
        
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }
} 