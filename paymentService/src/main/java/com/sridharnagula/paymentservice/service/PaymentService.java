package com.sridharnagula.paymentservice.service;

import com.razorpay.RazorpayException;
import com.sridharnagula.paymentservice.dto.PaymentResponse;

public interface PaymentService {
    String  doPayment(String email,String phone, Long amount,String orderId) throws RazorpayException;
}
