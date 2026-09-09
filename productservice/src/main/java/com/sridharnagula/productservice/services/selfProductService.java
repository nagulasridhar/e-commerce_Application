package com.sridharnagula.productservice.services;

import com.sridharnagula.productservice.dtos.CreateProductRequestDTO;
import com.sridharnagula.productservice.exceptions.ProductNotFoundException;
import com.sridharnagula.productservice.models.Category;
import com.sridharnagula.productservice.models.Product;
import com.sridharnagula.productservice.repositories.CategoryRepository;
import com.sridharnagula.productservice.repositories.ProductRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("selfProductService")
public class selfProductService implements ProductService{

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private final Tracer tracer;

    public selfProductService(ProductRepository productRepository, CategoryRepository categoryRepository, Tracer tracer){
        this.productRepository = productRepository;
        this.categoryRepository=categoryRepository;
        this.tracer = tracer;
    }

    @Override
    public Product getSingleProduct(Long productid) throws ProductNotFoundException{
        // This lookup is on the checkout hot path (orderservice calls it once per line
        // item while pricing an order), so it gets its own span rather than relying only
        // on the agent's controller-level span - makes a slow/failed catalog lookup easy
        // to spot as the latency source inside an order-placement trace in SigNoz.
        Span span = tracer.spanBuilder("catalog-service.get-product")
                .setAttribute("product.id", productid)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            Product product = productRepository.findByIdIs(productid);
            if(product==null){
                span.setStatus(StatusCode.ERROR, "product not found");
                throw new ProductNotFoundException("Product with Id "+ productid + " Cannot be found. Try to change the Id from single method");
            }
            return product;
        } catch (ProductNotFoundException e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
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
        return productRepository.findAll();
    }

    @Override
    public String deleteProduct(Long productId) throws ProductNotFoundException{
        Product productInDB = productRepository.findByIdIs(productId);
        if(productInDB==null){
            throw new ProductNotFoundException("Cannot find the id:"+productId+" which you would like to delete");
        }
        productRepository.deleteById(productId);
        return "Product Successfully deleted";
    }

    @Override
    public Product updateProduct(Long id, String title, String description, String category, double price, String image) throws ProductNotFoundException{
        Product product = getSingleProduct(id);
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
        Product savedProductInDB =productRepository.save(product);

        return savedProductInDB;
    }

    @Override
    public List<Product> getProductsByCategory(String category) throws ProductNotFoundException{
        List<Product> products = productRepository.findProductByCategoryTitle(category);
        if(products.size()==0){
            throw new ProductNotFoundException("Cannot find the Products with the category: "+ category);
        }
        return products;
    }

    @Override
    public Product patchProduct(Long productId, String title, String description, String category, Double price, String image) throws ProductNotFoundException {
        Product product = getSingleProduct(productId);
        // Don't need to throw new exception since getSingleProduct will throw if id not found
        if(title!=null){
            product.setTitle(title);
        }
        if(description!=null){
            product.setDescription(description);
        }
        if(price!=null){
            product.setPrice(price);
        }
        if(image!=null){
            product.setImageUrl(image);
        }
        if(category!=null){
            Category categoryInDB = categoryRepository.findByTitle(category);
            if(categoryInDB==null){
                Category newCategory = new Category();
                newCategory.setTitle(category);
                categoryInDB = newCategory;
            }
            product.setCategory(categoryInDB);
        }
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }
}
