package com.gabriel.pos_system.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.pos_system.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findTopByOrderByIdDesc();

    Page<Sale> findAllByOrderByIdDesc(Pageable pageable);

    Page<Sale> findByNumeroVentaContainingIgnoreCase(String numeroVenta, Pageable pageable);

    Page<Sale> findByFechaRegistroBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
