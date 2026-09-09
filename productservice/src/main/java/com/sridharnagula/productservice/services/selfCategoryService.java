package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.CategoryDTO;
import com.sridharnagula.productservice.exceptions.CategoryNotFoundException;
import com.sridharnagula.productservice.models.Category;
import com.sridharnagula.productservice.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("selfCategoryService")
public class selfCategoryService implements CategoryService{

    private final CategoryRepository categoryRepository;

    public selfCategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream().map(CategoryDTO::from).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " was not found"));
        return CategoryDTO.from(category);
    }

    @Override
    public CategoryDTO createCategory(String title) {
        Category existing = categoryRepository.findByTitle(title);
        if (existing != null) {
            return CategoryDTO.from(existing);
        }
        Category category = new Category();
        category.setTitle(title);
        return CategoryDTO.from(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO updateCategory(Long id, String title) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " was not found"));
        category.setTitle(title);
        return CategoryDTO.from(categoryRepository.save(category));
    }

    @Override
    public String deleteCategory(Long id) throws CategoryNotFoundException {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category with id " + id + " was not found");
        }
        categoryRepository.deleteById(id);
        return "Category successfully deleted";
    }
}
