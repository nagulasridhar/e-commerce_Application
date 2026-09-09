package com.sridharnagula.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String gateway;          // "razorpay" or "stripe"
    private String paymentReferenceId; // the gateway's payment-link / session id
    private String paymentUrl;         // hosted checkout page the customer is redirected to
    private String status;             // gateway's reported status, e.g. "created"
    private String orderId;
    private Long amount;
}
