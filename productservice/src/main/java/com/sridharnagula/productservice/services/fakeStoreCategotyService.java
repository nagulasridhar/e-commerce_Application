package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.FakeStoreProductDTO;
import com.sridharnagula.productservice.dtos.fakeStoreCategoryDTO;
import com.sridharnagula.productservice.models.Category;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class fakeStoreCategotyService implements CategoryService {
    private RestTemplate restTemplate;

    public fakeStoreCategotyService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    public List<String> getAllCategory() {
        String[] categories = restTemplate.getForObject(
                "https://fakestoreapi.com/products/categories",
                String[].class);

        return Arrays.asList(categories);
    }
}
