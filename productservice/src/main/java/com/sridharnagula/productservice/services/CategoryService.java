package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.CategoryDTO;
import com.sridharnagula.productservice.models.Category;

import java.util.List;

public interface CategoryService {

     List<CategoryDTO> getAllCategory();
}
