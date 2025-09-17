package com.gabriel.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.ClaveProdServSat;

@Repository
public interface ClaveProdServSatRepository extends JpaRepository<ClaveProdServSat, String> {

}
