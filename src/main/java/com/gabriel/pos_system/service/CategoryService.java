package com.gabriel.pos_system.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gabriel.pos_system.dto.CategoryDto;
import com.gabriel.pos_system.model.Category;

public interface CategoryService {

    // List<Category> findAllCategories();

    Page<Category> findPaginated(int page, int size, String searchTerm);

    void saveCategory(CategoryDto categoryDto);

}
