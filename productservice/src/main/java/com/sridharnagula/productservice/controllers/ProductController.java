package com.sridharnagula.productservice.controllers;

import com.sridharnagula.productservice.dtos.CreateProductRequestDTO;
import com.sridharnagula.productservice.dtos.FakeStoreProductDTO;
import com.sridharnagula.productservice.models.Product;
import com.sridharnagula.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(@Qualifier("selfProductService") ProductService productService){
        this.productService = productService;
    }
    @PostMapping("/products")
    public Product createproduct(@RequestBody CreateProductRequestDTO request){
         return  productService.createProduct(
                 request.getTitle(),
                 request.getDescription(),
                 request.getCategory(),
                 request.getPrice(),
                 request.getImage()
         );
    }

    @GetMapping("/products")
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductDetails(@PathVariable("id") Long productId){
        return productService.getSingleProduct(productId);
    }
    @GetMapping("/products/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category){
        return productService.getProductsByCategory(category);
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {

        return productService.deleteProduct(productId);
    }

    @PutMapping("/products/{id}")
    public String  updateProduct(@PathVariable("id") Long productId, @RequestBody FakeStoreProductDTO request){
        return productService.updateProduct(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getImage()
        );
    }
}
