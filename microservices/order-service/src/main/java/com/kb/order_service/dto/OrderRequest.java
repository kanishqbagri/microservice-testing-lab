package com.kb.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;
    
    @NotEmpty(message = "Shipping address is required")
    private String shippingAddress;
    
    @NotEmpty(message = "Billing address is required")
    private String billingAddress;
    
    @NotEmpty(message = "Order items are required")
    @Valid
    private List<OrderItemRequest> orderItems;
} 