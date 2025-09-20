package com.gabriel.pos_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.pos_system.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByActivo(Integer activo);

    Optional<Client> findByRfc(String rfc);
}
