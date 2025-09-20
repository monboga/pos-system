package com.gabriel.pos_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByEstadoAndCategoryEstado(Integer productEstado, Integer categoryEstado);

    Optional<Product> findByCodigoDeBarra(String codigoDeBarra);
}
