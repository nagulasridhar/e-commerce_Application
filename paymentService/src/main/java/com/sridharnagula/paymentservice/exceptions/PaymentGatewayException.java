package com.sridharnagula.paymentservice.exceptions;

public class PaymentGatewayException extends Exception {
    public PaymentGatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}
