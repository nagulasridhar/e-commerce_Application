package com.sridharnagula.productservice.dtos;

import com.sridharnagula.productservice.models.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class fakeStoreCategoryDTO {
    private Long id;
    private String title;

    public Category toCategory(){
        Category category = new Category();
        setId(id);
        setTitle(title);
        return category;
    }
}
