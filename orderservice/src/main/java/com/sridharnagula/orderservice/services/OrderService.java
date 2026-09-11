package com.sridharnagula.orderservice.services;

import com.sridharnagula.orderservice.dtos.CreateOrderRequestDTO;
import com.sridharnagula.orderservice.exceptions.OrderNotFoundException;
import com.sridharnagula.orderservice.exceptions.PaymentFailedException;
import com.sridharnagula.orderservice.exceptions.RemoteProductNotFoundException;
import com.sridharnagula.orderservice.exceptions.RemoteUserNotFoundException;
import com.sridharnagula.orderservice.models.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(CreateOrderRequestDTO request)
            throws RemoteUserNotFoundException, RemoteProductNotFoundException, PaymentFailedException;

    Order getOrderById(Long orderId) throws OrderNotFoundException;

    List<Order> getOrdersForUser(Long userId);
}
