package com.gabriel.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.ImpuestoSat;

@Repository
public interface ImpuestoSatRepository extends JpaRepository<ImpuestoSat, String> {

}
