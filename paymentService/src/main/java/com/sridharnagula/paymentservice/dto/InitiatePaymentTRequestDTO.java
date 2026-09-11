package com.sridharnagula.paymentservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiatePaymentTRequestDTO {
    @NotBlank(message = "emial (email) is required")
    @Email(message = "emial must be a valid email address")
    private String emial;

    private String phoneNumber;

    @Positive(message = "amount must be a positive number of currency subunits")
    private Long amount;

    @NotBlank(message = "orderId is required")
    private String orderId;
}
