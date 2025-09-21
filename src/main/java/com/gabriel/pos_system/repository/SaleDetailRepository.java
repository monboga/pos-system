package com.gabriel.pos_system.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabriel.pos_system.model.SaleDetail;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {
    // Query para obtener los productos más vendidos (descripción y cantidad total)
    // Usamos Pageable para limitar los resultados (ej. Top 5)
    @Query("SELECT sd.product.descripcion, SUM(sd.cantidad) as totalQuantity FROM SaleDetail sd GROUP BY sd.product.descripcion ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);

}
