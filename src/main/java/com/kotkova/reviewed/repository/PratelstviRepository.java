package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.Pratelstvi;
import com.kotkova.reviewed.model.PratelstviKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PratelstviRepository extends JpaRepository<Pratelstvi, PratelstviKey> {

    // Najde žádosti, které čekají na schválení aktuálním uživatelem (příjemcem)
    List<Pratelstvi> findByPrijemceIdUzivateleAndStavIdStavu(Long idPrijemce, Long idStavu);

    // Najde všechna potvrzená přátelství, kde figuruješ jako žadatel nebo příjemce
    @Query("SELECT p FROM Pratelstvi p WHERE (p.zadatel.idUzivatele = :id OR p.prijemce.idUzivatele = :id) AND p.stav.idStavu = 2")
    List<Pratelstvi> najdiPotvrzenePratele(@Param("id") Long idUzivatele);
}