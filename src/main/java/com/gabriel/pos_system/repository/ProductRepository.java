package com.gabriel.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
