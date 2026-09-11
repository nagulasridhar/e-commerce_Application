package com.sridharnagula.paymentservice.service;

import com.sridharnagula.paymentservice.dto.PaymentResponse;
import com.sridharnagula.paymentservice.exceptions.PaymentGatewayException;

public interface PaymentService {
    PaymentResponse doPayment(String email, String phone, Long amount, String orderId) throws PaymentGatewayException;
}
