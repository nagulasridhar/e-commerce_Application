package com.sridharnagula.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

/** Mirrors the fields of productservice's Product entity that GET /products/{id} returns. */
@Getter
@Setter
public class RemoteProductDTO {
    private Long id;
    private String title;
    private double price;
}
