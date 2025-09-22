package com.gabriel.pos_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gabriel.pos_system.dto.CategoryDto;
import com.gabriel.pos_system.model.Category;
import com.gabriel.pos_system.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // @Override
    // public List<Category> findAllCategories() {
    // return categoryRepository.findAll();
    // }

    @Override
    public void saveCategory(CategoryDto dto) {
        Category category;
        if (dto.getId() != null) {
            // Modo Edición
            category = categoryRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        } else {
            // Modo Creación
            category = new Category();
        }

        category.setDescripcion(dto.getDescripcion());
        category.setEstado(dto.getEstado());

        categoryRepository.save(category);
    }

    @Override
    public Page<Category> findPaginated(int page, int size, String searchTerm) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return categoryRepository.findBySearchTerm(searchTerm.trim(), pageable);
        } else {
            return categoryRepository.findAll(pageable);
        }
    }
}
