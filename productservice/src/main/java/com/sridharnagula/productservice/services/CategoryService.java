package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.CategoryDTO;
import com.sridharnagula.productservice.exceptions.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {

     List<CategoryDTO> getAllCategory();

     CategoryDTO getCategoryById(Long id) throws CategoryNotFoundException;

     CategoryDTO createCategory(String title);

     CategoryDTO updateCategory(Long id, String title) throws CategoryNotFoundException;

     String deleteCategory(Long id) throws CategoryNotFoundException;
}
