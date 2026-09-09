package com.sridharnagula.productservice.controllers;

import com.sridharnagula.productservice.dtos.CategoryDTO;
import com.sridharnagula.productservice.dtos.CreateCategoryRequestDTO;
import com.sridharnagula.productservice.exceptions.CategoryNotFoundException;
import com.sridharnagula.productservice.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(@Qualifier("selfCategoryService") CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping({"/products/category", "/categories"})
    public List<CategoryDTO> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @GetMapping("/categories/{id}")
    public CategoryDTO getCategoryById(@PathVariable("id") Long id) throws CategoryNotFoundException {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/categories")
    public CategoryDTO createCategory(@Valid @RequestBody CreateCategoryRequestDTO request) {
        return categoryService.createCategory(request.getTitle());
    }

    @PutMapping("/categories/{id}")
    public CategoryDTO updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CreateCategoryRequestDTO request) throws CategoryNotFoundException {
        return categoryService.updateCategory(id, request.getTitle());
    }

    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable("id") Long id) throws CategoryNotFoundException {
        return categoryService.deleteCategory(id);
    }
}
