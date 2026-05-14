package com.kotkova.reviewed.repository;

import com.kotkova.reviewed.model.Podnik;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PodnikRepository extends JpaRepository<Podnik, Long> {

    List<Podnik> findTop3ByOrderByIdPodnikuDesc();

    @Query(value = "SELECT p.* FROM PODNIKY p " +
            "LEFT JOIN (SELECT ID_PODNIKU, MAX(PRUMERNE_HODNOCENI) as PRUMERNE_HODNOCENI, MAX(POCET_RECENZI) as POCET_RECENZI FROM VW_PODNIKY_STATS GROUP BY ID_PODNIKU) v ON p.ID_PODNIKU = v.ID_PODNIKU " +
            "WHERE (:typId IS NULL OR p.ID_TYPU_PODNIKU = :typId) " +
            "AND (:tagCount IS NULL OR p.ID_PODNIKU IN (" +
            "    SELECT ID_PODNIKU FROM VW_STITKY_PODNIKU " +
            "    WHERE NAZEV IN (:tags) " +
            "    GROUP BY ID_PODNIKU " +
            "    HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
            ")) ORDER BY p.ID_PODNIKU DESC",
            countQuery = "SELECT count(*) FROM PODNIKY p " +
                    "WHERE (:typId IS NULL OR p.ID_TYPU_PODNIKU = :typId) " +
                    "AND (:tagCount IS NULL OR p.ID_PODNIKU IN (" +
                    "    SELECT ID_PODNIKU FROM VW_STITKY_PODNIKU WHERE NAZEV IN (:tags) GROUP BY ID_PODNIKU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
                    "))",
            nativeQuery = true)
    Page<Podnik> najdiFiltrovanePodniky(
            @Param("typId") Long typId,
            @Param("tags") List<String> tags,
            @Param("tagCount") Long tagCount,
            Pageable pageable);

    @Query(value = "SELECT p.* FROM PODNIKY p " +
            "LEFT JOIN (SELECT ID_PODNIKU, MAX(PRUMERNE_HODNOCENI) as PRUMERNE_HODNOCENI, MAX(POCET_RECENZI) as POCET_RECENZI FROM VW_PODNIKY_STATS GROUP BY ID_PODNIKU) v ON p.ID_PODNIKU = v.ID_PODNIKU " +
            "WHERE (:typId IS NULL OR p.ID_TYPU_PODNIKU = :typId) " +
            "AND (:tagCount IS NULL OR p.ID_PODNIKU IN (" +
            "    SELECT ID_PODNIKU FROM VW_STITKY_PODNIKU WHERE NAZEV IN (:tags) GROUP BY ID_PODNIKU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
            ")) ORDER BY p.NAZEV ASC, p.ID_PODNIKU DESC",
            countQuery = "SELECT count(*) FROM PODNIKY p " +
                    "WHERE (:typId IS NULL OR p.ID_TYPU_PODNIKU = :typId) " +
                    "AND (:tagCount IS NULL OR p.ID_PODNIKU IN (" +
                    "    SELECT ID_PODNIKU FROM VW_STITKY_PODNIKU WHERE NAZEV IN (:tags) GROUP BY ID_PODNIKU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
                    "))",
            nativeQuery = true)
    Page<Podnik> najdiPodnikyAbecedne(
            @Param("typId") Long typId,
            @Param("tags") List<String> tags,
            @Param("tagCount") Long tagCount,
            Pageable pageable);

    @Query(value = "SELECT p.* FROM PODNIKY p " +
            "LEFT JOIN (SELECT ID_PODNIKU, MAX(PRUMERNE_HODNOCENI) as PRUMERNE_HODNOCENI, MAX(POCET_RECENZI) as POCET_RECENZI FROM VW_PODNIKY_STATS GROUP BY ID_PODNIKU) v ON p.ID_PODNIKU = v.ID_PODNIKU " +
            "WHERE (:typId IS NULL OR p.ID_TYPU_PODNIKU = :typId) " +
            "AND (:tagCount IS NULL OR p.ID_PODNIKU IN (" +
            "    SELECT ID_PODNIKU FROM VW_STITKY_PODNIKU WHERE NAZEV IN (:tags) GROUP BY ID_PODNIKU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
            ")) ORDER BY v.PRUMERNE_HODNOCENI DESC NULLS LAST, p.ID_PODNIKU DESC",
            countQuery = "SELECT count(*) FROM PODNIKY p " +
                    "WHERE (:typId IS NULL OR p.ID_TYPU_PODNIKU = :typId) " +
                    "AND (:tagCount IS NULL OR p.ID_PODNIKU IN (" +
                    "    SELECT ID_PODNIKU FROM VW_STITKY_PODNIKU WHERE NAZEV IN (:tags) GROUP BY ID_PODNIKU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
                    "))",
            nativeQuery = true)
    Page<Podnik> najdiPodnikyPodleRatingu(
            @Param("typId") Long typId,
            @Param("tags") List<String> tags,
            @Param("tagCount") Long tagCount,
            Pageable pageable);

    @Query(value = "SELECT p.* FROM PODNIKY p " +
            "LEFT JOIN (SELECT ID_PODNIKU, MAX(PRUMERNE_HODNOCENI) as PRUMERNE_HODNOCENI, MAX(POCET_RECENZI) as POCET_RECENZI FROM VW_PODNIKY_STATS GROUP BY ID_PODNIKU) v ON p.ID_PODNIKU = v.ID_PODNIKU " +
            "WHERE (:typId IS NULL OR p.ID_TYPU_PODNIKU = :typId) " +
            "AND (:tagCount IS NULL OR p.ID_PODNIKU IN (" +
            "    SELECT ID_PODNIKU FROM VW_STITKY_PODNIKU WHERE NAZEV IN (:tags) GROUP BY ID_PODNIKU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
            ")) ORDER BY v.POCET_RECENZI DESC NULLS LAST, p.ID_PODNIKU DESC",
            countQuery = "SELECT count(*) FROM PODNIKY p " +
                    "WHERE (:typId IS NULL OR p.ID_TYPU_PODNIKU = :typId) " +
                    "AND (:tagCount IS NULL OR p.ID_PODNIKU IN (" +
                    "    SELECT ID_PODNIKU FROM VW_STITKY_PODNIKU WHERE NAZEV IN (:tags) GROUP BY ID_PODNIKU HAVING COUNT(DISTINCT ID_STITKU) >= :tagCount" +
                    "))",
            nativeQuery = true)
    Page<Podnik> najdiPodnikyPodlePopularity(
            @Param("typId") Long typId,
            @Param("tags") List<String> tags,
            @Param("tagCount") Long tagCount,
            Pageable pageable);
}