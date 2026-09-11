package com.sridharnagula.orderservice.dtos;

import com.sridharnagula.orderservice.models.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDTO {
    private Long productId;
    private String productTitle;
    private int quantity;
    private double unitPrice;

    public static OrderItemResponseDTO from(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setProductId(item.getProductId());
        dto.setProductTitle(item.getProductTitle());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }
}
