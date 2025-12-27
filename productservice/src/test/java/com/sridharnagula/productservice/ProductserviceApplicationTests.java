package com.sridharnagula.productservice;

import com.sridharnagula.productservice.repositories.CategoryRepository;
import com.sridharnagula.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductserviceApplicationTests {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	CategoryRepository categoryRepository;

	@Test
	void contextLoads() {
	}
	@Test
	void testingQueries(){
		// productRepository.findProductByCategoryTitle("string");
		categoryRepository.findAll();
	}

}
