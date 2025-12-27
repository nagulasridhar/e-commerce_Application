package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.CreateProductRequestDTO;
import com.sridharnagula.productservice.exceptions.ProductNotFoundException;
import com.sridharnagula.productservice.models.Product;

import java.util.List;

public interface ProductService {

    Product getSingleProduct(Long productid)  throws ProductNotFoundException;

    Product createProduct(String title,
                          String description,
                          String Category,
                          double price,
                          String image);
    List<Product> getProducts();

    String deleteProduct(Long productId) throws ProductNotFoundException;

    Product updateProduct(Long id,
                          String title,
                          String description,
                          String category,
                          double price,
                          String image) throws ProductNotFoundException;


    List<Product> getProductsByCategory(String category)throws ProductNotFoundException;

    Product patchProduct(Long productId, String title, String description, String category, double price, String image) throws ProductNotFoundException;
}
