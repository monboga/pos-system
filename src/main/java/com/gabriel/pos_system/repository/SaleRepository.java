package com.gabriel.pos_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.pos_system.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findTopByOrderByIdDesc();

    List<Sale> findAllByOrderByIdDesc();
}
