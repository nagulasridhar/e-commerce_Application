package com.sridharnagula.productservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDTO {

    @NotBlank(message = "title is required")
    private String title;

    @Positive(message = "price must be greater than 0")
    private double price;

    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "category is required")
    private String category;

    private String image;
}


// DTO for each Request so that in future
// if the request needs Additional parameters
// you can easily add without impact anything else
