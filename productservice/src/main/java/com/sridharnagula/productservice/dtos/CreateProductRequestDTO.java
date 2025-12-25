package com.sridharnagula.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDTO {
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
}


// DTO for each Request so that in future
// if the request needs Additional parameters
// you can easily add without impact anything else