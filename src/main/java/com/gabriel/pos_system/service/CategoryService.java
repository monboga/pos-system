package com.gabriel.pos_system.service;

import java.util.List;

import com.gabriel.pos_system.dto.CategoryDto;
import com.gabriel.pos_system.model.Category;

public interface CategoryService {

    List<Category> findAllCategories();

    void saveCategory(CategoryDto categoryDto);

}
