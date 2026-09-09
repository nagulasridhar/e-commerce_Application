package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.CategoryDTO;
import com.sridharnagula.productservice.exceptions.CategoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreCategoryService")
public class fakeStoreCategotyService implements CategoryService {
    private RestTemplate restTemplate;

    public fakeStoreCategotyService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        String[] categories = restTemplate.getForObject(
                "https://fakestoreapi.com/products/categories",
                String[].class);
        List<CategoryDTO> categoryDTO = new ArrayList<>();
        for(String title : categories){
            CategoryDTO dto = new CategoryDTO();
            dto.setTitle(title);
            categoryDTO.add(dto);
        }
        return categoryDTO;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) throws CategoryNotFoundException {
        throw new UnsupportedOperationException("Fake Store API categories aren't id-addressable; use getAllCategory().");
    }

    @Override
    public CategoryDTO createCategory(String title) {
        throw new UnsupportedOperationException("Fake Store API does not support creating categories.");
    }

    @Override
    public CategoryDTO updateCategory(Long id, String title) throws CategoryNotFoundException {
        throw new UnsupportedOperationException("Fake Store API does not support updating categories.");
    }

    @Override
    public String deleteCategory(Long id) throws CategoryNotFoundException {
        throw new UnsupportedOperationException("Fake Store API does not support deleting categories.");
    }
}
