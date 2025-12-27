package com.sridharnagula.productservice.controllers;

import com.sridharnagula.productservice.dtos.CategoryDTO;
import com.sridharnagula.productservice.models.Category;
import com.sridharnagula.productservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(@Qualifier("selfCategoryService") CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/products/category")
    public List<CategoryDTO> getAllCategory(){
        return categoryService.getAllCategory();
    }
}
