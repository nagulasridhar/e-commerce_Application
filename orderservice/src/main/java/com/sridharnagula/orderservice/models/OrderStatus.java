package com.sridharnagula.orderservice.models;

public enum OrderStatus {
    CREATED,
    USER_VERIFIED,
    ITEMS_PRICED,
    PAYMENT_INITIATED,
    CONFIRMED,
    FAILED
}
