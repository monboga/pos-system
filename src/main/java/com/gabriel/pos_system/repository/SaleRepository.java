package com.gabriel.pos_system.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabriel.pos_system.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findTopByOrderByIdDesc();

    Page<Sale> findAllByOrderByIdDesc(Pageable pageable);

    Page<Sale> findByNumeroVentaContainingIgnoreCase(String numeroVenta, Pageable pageable);

    Page<Sale> findByFechaRegistroBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Query para sumar el total de todas las ventas (Ingresos Totales)
    @Query("SELECT SUM(s.total) FROM Sale s")
    Double findTotalRevenue();

    // Query para obtener la suma de ventas agrupadas por día de los últimos 7 días
    @Query("SELECT FUNCTION('DATE', s.fechaRegistro), SUM(s.total) FROM Sale s WHERE s.fechaRegistro >= :startDate GROUP BY FUNCTION('DATE', s.fechaRegistro) ORDER BY FUNCTION('DATE', s.fechaRegistro) ASC")
    List<Object[]> findSalesLast7Days(LocalDateTime startDate);
}
