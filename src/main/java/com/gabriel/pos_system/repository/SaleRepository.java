package com.gabriel.pos_system.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.pos_system.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findTopByOrderByIdDesc();

    List<Sale> findAllByOrderByIdDesc();

    // Busca ventas cuyo número de venta contenga el texto proporcionado (ignorando
    // mayúsculas/minúsculas).
    List<Sale> findByNumeroVentaContainingIgnoreCase(String numeroVenta);

    // Busca ventas cuya fecha de registro se encuentre entre dos fechas dadas.
    List<Sale> findByFechaRegistroBetween(LocalDateTime start, LocalDateTime end);
}
