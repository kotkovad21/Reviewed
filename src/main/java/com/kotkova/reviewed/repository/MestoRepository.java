package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.Mesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MestoRepository extends JpaRepository<Mesto, Long> {
    // Tady taky základ
}