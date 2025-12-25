package com.sridharnagula.productservice.controllers;

import com.sridharnagula.productservice.models.Category;
import com.sridharnagula.productservice.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/products/category")
    public List<String> getAllCategory(){
        return categoryService.getAllCategory();
    }
}
