package com.sridharnagula.orderservice.controllers;

import com.sridharnagula.orderservice.dtos.CreateOrderRequestDTO;
import com.sridharnagula.orderservice.dtos.OrderResponseDTO;
import com.sridharnagula.orderservice.exceptions.OrderNotFoundException;
import com.sridharnagula.orderservice.exceptions.PaymentFailedException;
import com.sridharnagula.orderservice.exceptions.RemoteProductNotFoundException;
import com.sridharnagula.orderservice.exceptions.RemoteUserNotFoundException;
import com.sridharnagula.orderservice.models.Order;
import com.sridharnagula.orderservice.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDTO createOrder(@Valid @RequestBody CreateOrderRequestDTO request)
            throws RemoteUserNotFoundException, RemoteProductNotFoundException, PaymentFailedException {
        Order order = orderService.createOrder(request);
        return OrderResponseDTO.from(order);
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable("id") Long orderId) throws OrderNotFoundException {
        return OrderResponseDTO.from(orderService.getOrderById(orderId));
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponseDTO> getOrdersForUser(@PathVariable("userId") Long userId) {
        return orderService.getOrdersForUser(userId).stream().map(OrderResponseDTO::from).collect(Collectors.toList());
    }
}
