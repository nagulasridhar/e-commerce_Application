package com.sridharnagula.productservice.dtos;

import com.sridharnagula.productservice.models.Category;
import com.sridharnagula.productservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDTO {
    private Long id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;

    public Product toProduct(){
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setDescription(description);
        product.setImageUrl(image);
        Category productCategory = new Category();
        productCategory.setTitle(category);
        product.setCategory(productCategory);

        return product;
        }
}
