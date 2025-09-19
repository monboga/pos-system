package com.gabriel.pos_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByEstado(Integer estado);
}
