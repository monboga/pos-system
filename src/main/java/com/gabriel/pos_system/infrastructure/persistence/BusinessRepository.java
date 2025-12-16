package com.gabriel.pos_system.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

}
