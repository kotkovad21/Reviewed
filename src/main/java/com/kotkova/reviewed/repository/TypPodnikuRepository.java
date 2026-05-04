package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.TypPodniku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypPodnikuRepository extends JpaRepository<TypPodniku, Long> {
    // JpaRepository nám automaticky vytvoří metody jako findAll()
}