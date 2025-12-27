package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.CategoryDTO;
import com.sridharnagula.productservice.models.Category;
import com.sridharnagula.productservice.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("selfCategoryService")
public class selfCategoryService implements CategoryService{

    CategoryRepository  categoryRepository;
    public selfCategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;

    }
    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDtos = new ArrayList<>();
        for(Category category : categories){
            CategoryDTO dto = new CategoryDTO();
            dto.setTitle(category.getTitle());
            categoryDtos.add(dto);
        }
        return categoryDtos;
    }
}
