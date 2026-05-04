package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.Recenze;
import com.kotkova.reviewed.model.Uzivatel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecenzeRepository extends JpaRepository<Recenze, Long> {

    List<Recenze> findTop6ByOrderByIdObsahuDesc();

    List<Recenze> findByPodnikIdPodniku(Long idPodniku);

    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
            "WHERE v.ID_VIDITELNOSTI IN (1, 2) OR v.AUTOR_ID = :mojeId " +
            "OR (v.ID_VIDITELNOSTI = 3 AND EXISTS (SELECT 1 FROM VW_AKTIVNI_PRATELE f WHERE f.UZIVATEL_ID = :mojeId AND f.PRITEL_ID = v.AUTOR_ID)) " +
            "ORDER BY r.ID_OBSAHU DESC",
            countQuery = "SELECT count(*) FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
                    "WHERE v.ID_VIDITELNOSTI IN (1, 2) OR v.AUTOR_ID = :mojeId " +
                    "OR (v.ID_VIDITELNOSTI = 3 AND EXISTS (SELECT 1 FROM VW_AKTIVNI_PRATELE f WHERE f.UZIVATEL_ID = :mojeId AND f.PRITEL_ID = v.AUTOR_ID))",
            nativeQuery = true)
    Page<Recenze> najdiViditelneRecenze(@Param("mojeId") Long mojeId, Pageable pageable);

    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
            "WHERE v.ID_VIDITELNOSTI IN (1, 2) ORDER BY r.ID_OBSAHU DESC",
            countQuery = "SELECT count(*) FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU WHERE v.ID_VIDITELNOSTI IN (1, 2)",
            nativeQuery = true)
    Page<Recenze> najdiVerejneRecenze(Pageable pageable);

    // Zde Spring Data JPA zvládne vygenerovat count samo, protože to není nativeQuery
    Page<Recenze> findByObsahUzivatelIdUzivateleOrderByIdObsahuDesc(Long idUzivatele, Pageable pageable);

    // List nevrací Page, nepotřebuje countQuery
    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
            "WHERE v.ID_PODNIKU = :idPodniku AND (v.ID_VIDITELNOSTI IN (1, 2) OR v.AUTOR_ID = :mojeId " +
            "OR (v.ID_VIDITELNOSTI = 3 AND EXISTS (SELECT 1 FROM VW_AKTIVNI_PRATELE f WHERE f.UZIVATEL_ID = :mojeId AND f.PRITEL_ID = v.AUTOR_ID))) " +
            "ORDER BY r.ID_OBSAHU DESC", nativeQuery = true)
    List<Recenze> najdiViditelneRecenzePodniku(@Param("idPodniku") Long idPodniku, @Param("mojeId") Long mojeId);

    // List nevrací Page, nepotřebuje countQuery
    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
            "WHERE v.ID_PODNIKU = :idPodniku AND v.ID_VIDITELNOSTI IN (1, 2) ORDER BY r.ID_OBSAHU DESC", nativeQuery = true)
    List<Recenze> najdiVerejneRecenzePodniku(@Param("idPodniku") Long idPodniku);

    List<Recenze> findByObsah_UzivatelOrderByIdObsahuDesc(Uzivatel uzivatel);

    // 1. Osobní filtrované recenze
    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
            "WHERE v.AUTOR_ID = :mojeId " +
            "AND (:typId IS NULL OR v.ID_TYPU_PODNIKU = :typId) " +
            "AND (:tagCount IS NULL OR r.ID_OBSAHU IN (" +
            "    SELECT ID_OBSAHU FROM VW_STITKY_OBSAHU " +
            "    WHERE NAZEV IN (:tags) " +
            "    GROUP BY ID_OBSAHU " +
            "    HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
            ")) ORDER BY r.ID_OBSAHU DESC",
            countQuery = "SELECT count(*) FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
                    "WHERE v.AUTOR_ID = :mojeId " +
                    "AND (:typId IS NULL OR v.ID_TYPU_PODNIKU = :typId) " +
                    "AND (:tagCount IS NULL OR r.ID_OBSAHU IN (" +
                    "    SELECT ID_OBSAHU FROM VW_STITKY_OBSAHU WHERE NAZEV IN (:tags) GROUP BY ID_OBSAHU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
                    "))",
            nativeQuery = true)
    Page<Recenze> najdiMojeFiltrovaneRecenze(@Param("mojeId") Long mojeId, @Param("typId") Long typId, @Param("tags") List<String> tags, @Param("tagCount") Long tagCount, Pageable pageable);

    // 2. Globální filtrované recenze
    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
            "WHERE (v.ID_VIDITELNOSTI IN (1, 2) OR v.AUTOR_ID = :mojeId " +
            "OR (v.ID_VIDITELNOSTI = 3 AND EXISTS (SELECT 1 FROM VW_AKTIVNI_PRATELE f WHERE f.UZIVATEL_ID = :mojeId AND f.PRITEL_ID = v.AUTOR_ID))) " +
            "AND (:typId IS NULL OR v.ID_TYPU_PODNIKU = :typId) " +
            "AND (:tagCount IS NULL OR r.ID_OBSAHU IN (" +
            "    SELECT ID_OBSAHU FROM VW_STITKY_OBSAHU WHERE NAZEV IN (:tags) GROUP BY ID_OBSAHU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
            ")) ORDER BY r.ID_OBSAHU DESC",
            countQuery = "SELECT count(*) FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
                    "WHERE (v.ID_VIDITELNOSTI IN (1, 2) OR v.AUTOR_ID = :mojeId " +
                    "OR (v.ID_VIDITELNOSTI = 3 AND EXISTS (SELECT 1 FROM VW_AKTIVNI_PRATELE f WHERE f.UZIVATEL_ID = :mojeId AND f.PRITEL_ID = v.AUTOR_ID))) " +
                    "AND (:typId IS NULL OR v.ID_TYPU_PODNIKU = :typId) " +
                    "AND (:tagCount IS NULL OR r.ID_OBSAHU IN (SELECT ID_OBSAHU FROM VW_STITKY_OBSAHU WHERE NAZEV IN (:tags) GROUP BY ID_OBSAHU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount))",
            nativeQuery = true)
    Page<Recenze> najdiGlobalniFiltrovaneRecenze(@Param("mojeId") Long mojeId, @Param("typId") Long typId, @Param("tags") List<String> tags, @Param("tagCount") Long tagCount, Pageable pageable);

    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU ORDER BY r.ID_OBSAHU DESC",
            countQuery = "SELECT count(*) FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU",
            nativeQuery = true)
    Page<Recenze> najdiVsechnyRecenzeProAdmina(Pageable pageable);

    // List nevrací Page, nepotřebuje countQuery
    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU WHERE v.ID_PODNIKU = :idPodniku ORDER BY r.ID_OBSAHU DESC", nativeQuery = true)
    List<Recenze> najdiVsechnyRecenzePodnikuProAdmina(@Param("idPodniku") Long idPodniku);

    // 1. Profil konkrétního uživatele - filtruje viditelnost podle toho, kdo se dívá
    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
            "WHERE v.AUTOR_ID = :idAutora AND (v.ID_VIDITELNOSTI IN (1, 2) OR :mojeId = v.AUTOR_ID " +
            "OR (v.ID_VIDITELNOSTI = 3 AND EXISTS (SELECT 1 FROM VW_AKTIVNI_PRATELE f WHERE f.UZIVATEL_ID = :mojeId AND f.PRITEL_ID = v.AUTOR_ID))) " +
            "ORDER BY r.ID_OBSAHU DESC", nativeQuery = true)
    List<Recenze> najdiViditelneRecenzeUzivatele(@Param("idAutora") Long idAutora, @Param("mojeId") Long mojeId);

    // 2. Profil uživatele z pohledu Admina (vidí vše kromě smazaných)
    @Query(value = "SELECT r.* FROM RECENZE r JOIN VW_RECENZE_DETAIL v ON r.ID_OBSAHU = v.ID_OBSAHU " +
            "WHERE v.AUTOR_ID = :idAutora ORDER BY r.ID_OBSAHU DESC", nativeQuery = true)
    List<Recenze> najdiVsechnyRecenzeUzivateleProAdmina(@Param("idAutora") Long idAutora);
}