package com.kotkova.reviewed.repository;
import com.kotkova.reviewed.model.Uzivatel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UzivatelRepository extends JpaRepository<Uzivatel, Long> {
    Optional<Uzivatel> findByEmail(String email);
    // Najde uživatele podle částečné shody přezdívky (bez ohledu na velikost písmen)
    List<Uzivatel> findByPrezdivkaContainingIgnoreCase(String query);

    // Vrátí true, pokud uživatel s tímto e-mailem už existuje
    boolean existsByEmail(String email);

    // Vrátí true, pokud uživatel s touto přezdívkou už existuje
    boolean existsByPrezdivka(String prezdivka);
}