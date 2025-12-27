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
    public Product updateProduct(Long id,
                                 String title,
                                 String description,
                                 String category,
                                 double price,
                                 String image) throws ProductNotFoundException{
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
        if(productFound!=false){
            throw new ProductNotFoundException("Product id: "+id+ "is not found");
        }
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
    public Product patchProduct(Long productId, String title, String description, String category, double price, String image) throws ProductNotFoundException {
        Product product = getSingleProduct(productId);

        return null;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductNotFoundException{
        ArrayList<Product> products = (ArrayList<Product>) getProducts();
        boolean productfound = false;
        for(Product p :products){
            if(p.getId()==productId){
                products.remove(productId);
                productfound=true;
                break;
            }
        }
        if(productfound==false) {
            throw new ProductNotFoundException("Cannot find the id:"+productId+" which you would like to delete");
        }
        return "Product is deleted";
    }
}
