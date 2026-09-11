package com.sridharnagula.orderservice.dtos;

import com.sridharnagula.orderservice.models.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private double totalAmount;
    private String status;
    private String paymentReference;
    private List<OrderItemResponseDTO> items;

    public static OrderResponseDTO from(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        dto.setPaymentReference(order.getPaymentReference());
        dto.setItems(order.getItems().stream().map(OrderItemResponseDTO::from).collect(Collectors.toList()));
        return dto;
    }
}
