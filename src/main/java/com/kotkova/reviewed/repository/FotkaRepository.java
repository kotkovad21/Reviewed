package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.Fotka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotkaRepository extends JpaRepository<Fotka, Long> {
    List<Fotka> findByIdRecenze(Long idRecenze);

    @Query("SELECT f.idFotky FROM Fotka f WHERE f.idRecenze = :idRecenze")
    List<Long> najdiIdFotekPodleRecenze(@Param("idRecenze") Long idRecenze);

    @Query("SELECT f.idFotky FROM Fotka f JOIN Recenze r ON f.idRecenze = r.idObsahu " +
            "WHERE r.podnik.idPodniku = :idPodniku ORDER BY r.idObsahu DESC")
    List<Long> najdiIdFotekPodlePodniku(@Param("idPodniku") Long idPodniku);
}