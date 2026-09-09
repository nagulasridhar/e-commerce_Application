package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.FakeStoreProductDTO;
import com.sridharnagula.productservice.exceptions.ProductNotFoundException;
import com.sridharnagula.productservice.models.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("fakeStoreProductService")
public class fakeStoreProductService implements ProductService {

    private RestTemplate restTemplate;

    public fakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productid) throws ProductNotFoundException{
        ResponseEntity<FakeStoreProductDTO> fakeStoreProductResponse =  restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + productid,
                FakeStoreProductDTO.class
        );
        FakeStoreProductDTO fakeStoreProduct = fakeStoreProductResponse.getBody();
        if(fakeStoreProduct==null){
            throw new ProductNotFoundException("Product with Id "+ productid + " Cannot be found. Try to change the Id");
        }
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

    @Override
    public Product updateProduct(Long id,
                                 String title,
                                 String description,
                                 String category,
                                 double price,
                                 String image) throws ProductNotFoundException{
        boolean productFound = getProducts().stream().anyMatch(p -> Objects.equals(p.getId(), id));
        if(!productFound){
            throw new ProductNotFoundException("Product id: "+id+" is not found");
        }

        FakeStoreProductDTO fakeStoreProductDTO = new FakeStoreProductDTO();
        fakeStoreProductDTO.setTitle(title);
        fakeStoreProductDTO.setCategory(category);
        fakeStoreProductDTO.setDescription(description);
        fakeStoreProductDTO.setPrice(price);
        fakeStoreProductDTO.setImage(image);

        //put method cannot give a response so we can use exchange method
        ResponseEntity<FakeStoreProductDTO> response = restTemplate.exchange("https://fakestoreapi.com/products/"+id,
                HttpMethod.PUT,
                new HttpEntity<>(fakeStoreProductDTO),
                FakeStoreProductDTO.class // data type of response
        );
        return response.getBody().toProduct();
    }

    @Override
    public List<Product> getProductsByCategory(String category) throws ProductNotFoundException {
        FakeStoreProductDTO[] productArray = restTemplate.getForObject("https://fakestoreapi.com/products/category/"+ category,
                FakeStoreProductDTO[].class);
        List<Product> categoryProducts = new ArrayList<>();
        for(FakeStoreProductDTO productDto : productArray){
            categoryProducts.add(productDto.toProduct());
        }
        if(categoryProducts.size()==0){
            throw new ProductNotFoundException("Cannot find the Products with the category: "+ category);
        }
        return categoryProducts;
    }

    @Override
    public Product patchProduct(Long productId, String title, String description, String category, Double price, String image) throws ProductNotFoundException {
        Product existing = getSingleProduct(productId);

        FakeStoreProductDTO patchDto = new FakeStoreProductDTO();
        patchDto.setTitle(title != null ? title : existing.getTitle());
        patchDto.setDescription(description != null ? description : existing.getDescription());
        patchDto.setCategory(category != null ? category : existing.getCategory().getTitle());
        patchDto.setPrice(price != null ? price : existing.getPrice());
        patchDto.setImage(image != null ? image : existing.getImageUrl());

        ResponseEntity<FakeStoreProductDTO> response = restTemplate.exchange(
                "https://fakestoreapi.com/products/" + productId,
                HttpMethod.PUT,
                new HttpEntity<>(patchDto),
                FakeStoreProductDTO.class
        );
        return response.getBody().toProduct();
    }

    @Override
    public String deleteProduct(Long productId) throws ProductNotFoundException{
        getSingleProduct(productId);
        restTemplate.delete("https://fakestoreapi.com/products/" + productId);
        return "Product is deleted";
    }
}
