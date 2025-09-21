package com.gabriel.pos_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabriel.pos_system.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByActivo(Integer activo);

    Optional<Client> findByRfc(String rfc);

    @Query("SELECT c FROM Client c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.rfc) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Client> findBySearchTerm(String searchTerm, Pageable pageable);

    Page<Client> findAll(Pageable pageable);
}
