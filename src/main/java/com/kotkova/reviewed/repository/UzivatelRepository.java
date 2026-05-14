package com.kotkova.reviewed.repository;
import com.kotkova.reviewed.model.Uzivatel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UzivatelRepository extends JpaRepository<Uzivatel, Long> {
    Optional<Uzivatel> findByEmail(String email);
    List<Uzivatel> findByPrezdivkaContainingIgnoreCase(String query);

    boolean existsByEmail(String email);

    boolean existsByPrezdivka(String prezdivka);
}