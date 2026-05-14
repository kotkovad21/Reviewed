package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.Pratelstvi;
import com.kotkova.reviewed.model.PratelstviKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PratelstviRepository extends JpaRepository<Pratelstvi, PratelstviKey> {

    List<Pratelstvi> findByPrijemceIdUzivateleAndStavIdStavu(Long idPrijemce, Long idStavu);

    @Query("SELECT p FROM Pratelstvi p WHERE (p.zadatel.idUzivatele = :id OR p.prijemce.idUzivatele = :id) AND p.stav.idStavu = 2")
    List<Pratelstvi> najdiPotvrzenePratele(@Param("id") Long idUzivatele);

    @Modifying
    @Transactional
    @Query("DELETE FROM Pratelstvi p WHERE " +
            "(p.zadatel.idUzivatele = :id1 AND p.prijemce.idUzivatele = :id2) OR " +
            "(p.zadatel.idUzivatele = :id2 AND p.prijemce.idUzivatele = :id1)")
    void smazPratelstviBezpecne(@Param("id1") Long id1, @Param("id2") Long id2);
}
