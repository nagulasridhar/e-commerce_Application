package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.models.Product;

import java.util.List;

public interface ProductService {

    Product getSingleProduct(Long productid);

    Product createProduct(String title,
                          String description,
                          String Category,
                          double price,
                          String image);
    List<Product> getProducts();

    String deleteProduct(Long productId);

    String updateProduct(Long id,
                          String title,
                          String description,
                          String category,
                          double price,
                          String image);
    List<Product> getProductsByCategory(String category);
}
