package com.sridharnagula.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequestDTO {
    private Long id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
}
