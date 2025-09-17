package com.gabriel.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.pos_system.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
