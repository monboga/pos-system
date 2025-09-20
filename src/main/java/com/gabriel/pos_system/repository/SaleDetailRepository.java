package com.gabriel.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.pos_system.model.SaleDetail;

public interface SalesDetailsRepository extends JpaRepository<SaleDetail, Long> {

}
