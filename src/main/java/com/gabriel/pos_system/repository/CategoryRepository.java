package com.gabriel.pos_system.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByEstado(Integer estado);

    @Query("SELECT c FROM Category c WHERE LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Category> findBySearchTerm(String searchTerm, Pageable pageable);

    @Override
    @NonNull
    Page<Category> findAll(@NonNull Pageable pageable);
}
