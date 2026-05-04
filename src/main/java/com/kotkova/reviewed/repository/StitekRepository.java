package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.Stitek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StitekRepository extends JpaRepository<Stitek, Long>{
    // Vytáhne všechny unikátní štítky ze všech recenzí daného podniku
    @Query(value = "SELECT s.* FROM STITKY s " +
            "JOIN VW_STITKY_PODNIKU v ON s.ID_STITKU = v.ID_STITKU " +
            "WHERE v.ID_PODNIKU = :idPodniku", nativeQuery = true)
    List<Stitek> najdiStitkyProPodnik(@Param("idPodniku") Long idPodniku);
}
