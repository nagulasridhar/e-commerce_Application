package com.sridharnagula.productservice.repositories;

import com.sridharnagula.productservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByTitle(String title);
    Category save(Category category);
    List<Category> findAll();

}
