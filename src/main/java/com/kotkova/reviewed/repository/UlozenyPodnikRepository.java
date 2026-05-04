package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.UlozenyPodnik;
import com.kotkova.reviewed.model.UlozenyPodnikId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UlozenyPodnikRepository extends JpaRepository<UlozenyPodnik, UlozenyPodnikId> {

    List<UlozenyPodnik> findByUzivatel_IdUzivateleOrderByDatumVytvoreniDesc(Long idUzivatele);

    Optional<UlozenyPodnik> findByUzivatel_IdUzivateleAndPodnik_IdPodniku(Long idUzivatele, Long idPodniku);
}