package com.sridharnagula.paymentservice.service;

import com.sridharnagula.paymentservice.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service("stripe")
public class StripePaymentServiceImpl implements PaymentService{

    @Override
    public String doPayment(String email, String phone, Long amount, String orderId) {
        return null;
    }
}
