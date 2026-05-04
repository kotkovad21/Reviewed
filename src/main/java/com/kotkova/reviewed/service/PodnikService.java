package com.kotkova.reviewed.service;


import com.kotkova.reviewed.model.Podnik;
import com.kotkova.reviewed.model.Recenze;
import com.kotkova.reviewed.model.UlozenyPodnik;
import com.kotkova.reviewed.model.Uzivatel;
import com.kotkova.reviewed.repository.RecenzeRepository;
import com.kotkova.reviewed.repository.PodnikRepository;
import com.kotkova.reviewed.repository.FotkaRepository;
import com.kotkova.reviewed.repository.UlozenyPodnikRepository;
import com.kotkova.reviewed.repository.StitekRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PodnikService {

    private final PodnikRepository podnikRepository;
    private final RecenzeRepository recenzeRepository;
    private final FotkaRepository fotkaRepository;
    private final UlozenyPodnikRepository ulozenyPodnikRepository;
    private final UzivatelService uzivatelService;
    private final StitekRepository stitekRepository;


    // Tímto říkáme Springu, ať nám repozitář "připraví"
    public PodnikService(PodnikRepository podnikRepository,
                         RecenzeRepository recenzeRepository,
                         FotkaRepository fotkaRepository,
                         UlozenyPodnikRepository ulozenyPodnikRepository,
                         UzivatelService uzivatelService,
                         StitekRepository stitekRepository) {
        this.podnikRepository = podnikRepository;
        this.recenzeRepository = recenzeRepository;
        this.fotkaRepository = fotkaRepository;
        this.ulozenyPodnikRepository = ulozenyPodnikRepository;
        this.uzivatelService = uzivatelService;
        this.stitekRepository = stitekRepository;
    }



    // Metoda, která vytáhne z databáze úplně všechny podniky
    public List<Podnik> ziskejVsechnyPodniky() {
        return podnikRepository.findAll();
    }

    public List<Podnik> ziskejNejnovejsiPodniky() {
        List<Podnik> podniky = podnikRepository.findTop3ByOrderByIdPodnikuDesc();

        for (Podnik p : podniky) {
            // 1. Vypočítáme průměr a uložíme ho do transientní proměnné
            p.setPrumernyRating(ziskejPrumernyRating(p.getIdPodniku()));

            // 2. Najdeme titulní fotku (vezmeme první recenzi, která má fotku)
            List<Recenze> recenzePodniku = recenzeRepository.findByPodnikIdPodniku(p.getIdPodniku());
            for (Recenze r : recenzePodniku) {
                List<Long> fotkaIds = fotkaRepository.najdiIdFotekPodleRecenze(r.getIdObsahu());
                if (!fotkaIds.isEmpty()) {
                    p.setIdTitulniFotky(fotkaIds.get(0));
                    break; // Jakmile najdeme jednu fotku, končíme hledání
                }
            }
        }
        return podniky;
    }
    public Podnik ziskejPodnikPodleId(Long id) {
        Podnik p = podnikRepository.findById(id).orElse(null);
        if (p != null) {
            // NOVÉ: Když otevřeme detail podniku, načteme mu jeho agregované štítky
            p.setStitky(stitekRepository.najdiStitkyProPodnik(p.getIdPodniku()));
        }
        return p;
    }

    public Double ziskejPrumernyRating(Long idPodniku) {
        List<Recenze> recenze = recenzeRepository.findByPodnikIdPodniku(idPodniku);
        if (recenze.isEmpty()) {
            return 0.0;
        }
        double soucet = 0;
        for (Recenze r : recenze) {
            soucet += r.getHodnoceni();
        }
        return soucet / recenze.size();
    }

    public void nastavTitulniFotku(Podnik p) {
        if (p == null) return;

        // Zavoláme ten nový dotaz, který hledá fotky v recenzích daného podniku[cite: 21]
        List<Long> fotkaIds = fotkaRepository.najdiIdFotekPodlePodniku(p.getIdPodniku());

        if (!fotkaIds.isEmpty()) {
            // Nastavíme první nalezenou fotku jako titulní
            p.setIdTitulniFotky(fotkaIds.get(0));
        }
    }

    public void ulozPodnikProUzivatele(Long idPodniku, Long idUzivatele) {
        // 1. KONTROLA: Už si tento uživatel tento podnik uložil?
        var existujici = ulozenyPodnikRepository.findByUzivatel_IdUzivateleAndPodnik_IdPodniku(idUzivatele, idPodniku);

        if (existujici.isPresent()) {
            // 2. Pokud už tam je, tak ho uživatel chce odebrat (Unsave)
            ulozenyPodnikRepository.delete(existujici.get());
        } else {
            // 3. Pokud tam není, vytvoříme novou vazbu (Save)
            UlozenyPodnik novy = new UlozenyPodnik();
            Uzivatel u = uzivatelService.ziskejUzivatelePodleId(idUzivatele);
            Podnik p = this.ziskejPodnikPodleId(idPodniku);

            if (u != null && p != null) {
                novy.setUzivatel(u);
                novy.setPodnik(p);
                novy.setDatumVytvoreni(LocalDate.now());
                ulozenyPodnikRepository.save(novy);
            }
        }
    }

    public List<UlozenyPodnik> ziskejUlozenePodnikyUzivatele(Long idUzivatele) {
        List<UlozenyPodnik> seznam = ulozenyPodnikRepository.findByUzivatel_IdUzivateleOrderByDatumVytvoreniDesc(idUzivatele);

        // Volitelně: Pokud chceš u podniků v seznamu rovnou vidět fotky jako u recenzí,
        // můžeš zde projít seznam a nastavit idTitulniFotky (stejně jako v RecenzeService)
        for (UlozenyPodnik up : seznam) {
            // Předpokládám metodu pro získání fotky v podnikService
            this.nastavTitulniFotku(up.getPodnik());
        }

        return seznam;
    }

    public Page<Podnik> ziskejFiltrovanePodniky(Pageable pageable, String business, List<String> tags, String order) {
        Long typId = (business == null || business.equals("all")) ? null : Long.parseLong(business);
        Long tagCount = (tags == null || tags.isEmpty()) ? null : (long) tags.size();

        Page<Podnik> stranka = switch (order != null ? order : "") {
            case "rating" -> podnikRepository.najdiPodnikyPodleRatingu(typId, tags, tagCount, pageable);
            case "abc" -> podnikRepository.najdiPodnikyAbecedne(typId, tags, tagCount, pageable);
            case "popular" -> podnikRepository.najdiPodnikyPodlePopularity(typId, tags, tagCount, pageable);
            default -> podnikRepository.najdiFiltrovanePodniky(typId, tags, tagCount, pageable);
        };

        for (Podnik p : stranka.getContent()) {
            this.nastavTitulniFotku(p);
            p.setPrumernyRating(ziskejPrumernyRating(p.getIdPodniku()));

        }
        return stranka;
    }

    // Přidej do PodnikService.java
    public boolean jePodnikUlozen(Long idPodniku, Long idUzivatele) {
        if (idUzivatele == null) return false;

        // Použijeme tvou existující metodu z repozitáře
        return ulozenyPodnikRepository
                .findByUzivatel_IdUzivateleAndPodnik_IdPodniku(idUzivatele, idPodniku)
                .isPresent();
    }
}