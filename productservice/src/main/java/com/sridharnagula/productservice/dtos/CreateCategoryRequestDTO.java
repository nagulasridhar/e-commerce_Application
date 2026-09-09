package com.sridharnagula.productservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDTO {
    @NotBlank(message = "title is required")
    private String title;
}
