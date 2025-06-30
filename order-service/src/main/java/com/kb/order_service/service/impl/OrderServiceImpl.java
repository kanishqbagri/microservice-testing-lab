package com.kb.order_service.service.impl;

import com.kb.order_service.dto.OrderItemRequest;
import com.kb.order_service.dto.OrderItemResponse;
import com.kb.order_service.dto.OrderRequest;
import com.kb.order_service.dto.OrderResponse;
import com.kb.order_service.entity.Order;
import com.kb.order_service.entity.OrderItem;
import com.kb.order_service.entity.OrderStatus;
import com.kb.order_service.exception.OrderNotFoundException;
import com.kb.order_service.repository.OrderRepository;
import com.kb.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.info("Creating order for user: {}", orderRequest.getUserId());
        
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setBillingAddress(orderRequest.getBillingAddress());
        
        // Create order items
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
            .map(this::createOrderItem)
            .collect(Collectors.toList());
        
        // Set order reference for each item
        orderItems.forEach(item -> item.setOrder(order));
        
        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        
        return convertToResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
        return convertToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order by order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with order number: " + orderNumber));
        return convertToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching orders with status: {}", status);
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        log.info("Updating order status for order ID: {} to status: {}", id, status);
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
        
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated successfully for order ID: {}", id);
        
        return convertToResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
        log.info("Order deleted successfully with ID: {}", id);
    }

    private OrderItem createOrderItem(OrderItemRequest itemRequest) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(itemRequest.getProductId());
        orderItem.setProductName("Product " + itemRequest.getProductId()); // This would come from product service
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setUnitPrice(BigDecimal.valueOf(10.00)); // This would come from product service
        orderItem.setTotalPrice(BigDecimal.valueOf(10.00).multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        return orderItem;
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = modelMapper.map(order, OrderResponse.class);
        
        if (order.getOrderItems() != null) {
            List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> modelMapper.map(item, OrderItemResponse.class))
                .collect(Collectors.toList());
            response.setOrderItems(itemResponses);
        }
        
        return response;
    }
} 