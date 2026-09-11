package com.sridharnagula.orderservice.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequestDTO {

    @Email(message = "userEmail must be valid")
    private String userEmail;

    @NotEmpty(message = "order must contain at least one item")
    private List<@Valid OrderItemRequestDTO> items;
}
