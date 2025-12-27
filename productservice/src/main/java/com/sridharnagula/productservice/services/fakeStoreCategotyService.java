package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.CategoryDTO;
import com.sridharnagula.productservice.dtos.FakeStoreProductDTO;
import com.sridharnagula.productservice.dtos.fakeStoreCategoryDTO;
import com.sridharnagula.productservice.models.Category;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("fakeStoreCategoryService")
public class fakeStoreCategotyService implements CategoryService {
    private RestTemplate restTemplate;

    public fakeStoreCategotyService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
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
}
