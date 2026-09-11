package com.sridharnagula.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiatePaymentRequestDTO {
    private String emial;
    private String phoneNumber;
    private Long amount;
    private String orderId;
}
