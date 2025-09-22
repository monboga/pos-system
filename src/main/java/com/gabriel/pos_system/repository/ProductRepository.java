package com.gabriel.pos_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.Product;

import jakarta.persistence.LockModeType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByEstadoAndCategoryEstado(Integer productEstado, Integer categoryEstado);

    Optional<Product> findByCodigoDeBarra(String codigoDeBarra);

    // Este método hace lo mismo que findById, pero le pide a la base de datos
    // que bloquee la fila encontrada para evitar que otras transacciones la
    // modifiquen.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(Long id);

    @Query("SELECT p FROM Product p WHERE LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> findBySearchTerm(String searchTerm, Pageable pageable);

    @Override
    @NonNull
    Page<Product> findAll(@NonNull Pageable pageable);
}
