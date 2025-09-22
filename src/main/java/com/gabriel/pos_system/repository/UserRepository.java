package com.gabriel.pos_system.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.gabriel.pos_system.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findBySearchTerm(String searchTerm, Pageable pageable);

    // Método para devolver todos los usuarios de forma paginada (sobrescribe el de
    // JpaRepository para claridad)
    @Override
    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);
}
