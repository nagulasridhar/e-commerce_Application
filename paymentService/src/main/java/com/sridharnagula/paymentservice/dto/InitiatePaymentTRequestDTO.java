package com.sridharnagula.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiatePaymentTRequestDTO {
    private String emial;
    private String phoneNumber;
    private Long amount;
    private  String orderId;
}
