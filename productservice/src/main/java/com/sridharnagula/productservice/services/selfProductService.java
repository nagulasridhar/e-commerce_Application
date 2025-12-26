package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.models.Category;
import com.sridharnagula.productservice.models.Product;
import com.sridharnagula.productservice.repositories.CategoryRepository;
import com.sridharnagula.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("selfProductService")
public class selfProductService implements ProductService{

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public selfProductService(ProductRepository productRepository, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository=categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long productid) {
        return null;
    }

    @Override
    public Product createProduct(String title, String description, String category, double price, String image) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(image);

        Category categoryFromDB = categoryRepository.findByTitle(category);
        if(categoryFromDB == null){
            Category newCategory = new Category();
            newCategory.setTitle(category);
            categoryFromDB = newCategory; // this is not yet saved in Database so will not have any id
        }
        product.setCategory(categoryFromDB);
        Product savedProductInDB = productRepository.save(product); //  while saving product if the category_id is NUll
                                //CascadeType.PERSIST will create that category and then save the product
        return savedProductInDB;
    }

    @Override
    public List<Product> getProducts() {
        return List.of();
    }

    @Override
    public String deleteProduct(Long productId) {
        return "";
    }

    @Override
    public String updateProduct(Long id, String title, String description, String category, double price, String image) {
        return "";
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return List.of();
    }
}
