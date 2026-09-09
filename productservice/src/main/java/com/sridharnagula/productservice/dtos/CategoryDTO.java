package com.sridharnagula.productservice.dtos;

import com.sridharnagula.productservice.models.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private Long id;
    private String title;

    public static CategoryDTO from(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        return dto;
    }
}
