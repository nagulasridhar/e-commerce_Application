package com.sridharnagula.orderservice.exceptions;

public class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
}
