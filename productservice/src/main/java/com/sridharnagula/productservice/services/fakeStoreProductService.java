package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.FakeStoreProductDTO;
import com.sridharnagula.productservice.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class fakeStoreProductService implements ProductService {

    private RestTemplate restTemplate;

    public fakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productid) {
        FakeStoreProductDTO fakeStoreProduct =  restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + productid,
                FakeStoreProductDTO.class
        );
        return fakeStoreProduct.toProduct();
    }

    @Override
    public Product createProduct(String title,
                                 String description,
                                 String category,
                                 double price,
                                 String image) {
        FakeStoreProductDTO fakeStoreProductDTO = new FakeStoreProductDTO();
        fakeStoreProductDTO.setTitle(title);
        fakeStoreProductDTO.setCategory(category);
        fakeStoreProductDTO.setDescription(description);
        fakeStoreProductDTO.setPrice(price);
        fakeStoreProductDTO.setImage(image);
        FakeStoreProductDTO response = restTemplate.postForObject("https://fakestoreapi.com/products",
                fakeStoreProductDTO, // request Body
                FakeStoreProductDTO.class // data type of response
                );
        return response.toProduct();
    }

    @Override
    public List<Product> getProducts() {
        FakeStoreProductDTO[] productArray = restTemplate.getForObject("https://fakestoreapi.com/products",
                FakeStoreProductDTO[].class
        ); // getting the list as Array
        List<Product> products = new ArrayList<>();
        for (FakeStoreProductDTO fakeStoreProductDTO : productArray) {
            products.add(fakeStoreProductDTO.toProduct());
        }
        return products;
    }
    public String updateProduct(Long id,
                                 String title,
                                 String description,
                                 String category,
                                 double price,
                                 String image){
        ArrayList<Product> products = (ArrayList<Product>) getProducts();
        FakeStoreProductDTO fakeStoreProductDTO = new FakeStoreProductDTO();
        fakeStoreProductDTO.setTitle(title);
        fakeStoreProductDTO.setCategory(category);
        fakeStoreProductDTO.setDescription(description);
        fakeStoreProductDTO.setPrice(price);
        fakeStoreProductDTO.setImage(image);
        boolean productFound = false;
        for(Product p :products){
            if(p.getId()==id){
                productFound = true;
            }
        }
        if(productFound){
            restTemplate.put("https://fakestoreapi.com/products/"+id,
                    fakeStoreProductDTO // request Body
                     // data type of response
            );
            return "Product Updated";
        }
        FakeStoreProductDTO response = restTemplate.postForObject("https://fakestoreapi.com/products",
                fakeStoreProductDTO, // request Body
                FakeStoreProductDTO.class // data type of response
        );


        return "No ProductId found. So created a New Product";
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        FakeStoreProductDTO[] productArray = restTemplate.getForObject("https://fakestoreapi.com/products/category/"+ category,
                FakeStoreProductDTO[].class);
        List<Product> categoryProducts = new ArrayList<>();
        for(FakeStoreProductDTO productDto : productArray){
            categoryProducts.add(productDto.toProduct());
        }
        return categoryProducts;
    }

    @Override
    public String deleteProduct(Long productId) {
        ArrayList<Product> products = (ArrayList<Product>) getProducts();
        boolean productfound = false;
        for(Product p :products){
            if(p.getId()==productId){
                products.remove(productId);
                productfound=true;
                break;
            }
        }
        if(productfound) {
            return "Deleted Successfully";
        }
        return "Product not found";
    }
}
