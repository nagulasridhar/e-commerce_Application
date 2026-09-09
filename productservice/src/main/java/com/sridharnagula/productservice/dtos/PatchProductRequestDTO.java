package com.sridharnagula.productservice.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchProductRequestDTO {
    private String title;

    @Positive(message = "price must be greater than 0")
    private Double price;

    private String description;
    private String category;
    private String image;
}
