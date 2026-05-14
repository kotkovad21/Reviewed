package com.kotkova.reviewed.service;

import com.kotkova.reviewed.model.Recenze;
import com.kotkova.reviewed.model.Uzivatel;
import com.kotkova.reviewed.model.Viditelnost;
import com.kotkova.reviewed.repository.FotkaRepository;
import com.kotkova.reviewed.repository.RecenzeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecenzeService {

    private final RecenzeRepository recenzeRepository;
    private final FotkaRepository fotkaRepository;

    public RecenzeService(RecenzeRepository recenzeRepository, FotkaRepository fotkaRepository) {
        this.recenzeRepository = recenzeRepository;
        this.fotkaRepository = fotkaRepository;
    }

    public Recenze ulozRecenzi(Recenze recenze) {
        return recenzeRepository.save(recenze);
    }

    public Recenze ziskejRecenziPodleId(Long id) {
        return recenzeRepository.findById(id).orElse(null);
    }
    public List<Recenze> ziskejVsechnyRecenze() {
        return recenzeRepository.findAll();
    }

    public List<Recenze> ziskejSestNejnovejsichRecenzi() {
        List<Recenze> seznam = recenzeRepository.findTop6ByOrderByIdObsahuDesc();
        for (Recenze r : seznam) {
            pripojIdTitulniFotky(r);
        }
        return seznam;
    }
    public Page<Recenze> ziskejStrankuRecenzi(Pageable pageable, Uzivatel prihlaseny) {
        if (prihlaseny != null && (prihlaseny.getRole().getIdRole() == 2 || prihlaseny.getRole().getIdRole() == 3)) {
            Page<Recenze> stranka = recenzeRepository.najdiVsechnyRecenzeProAdmina(pageable);
            for (Recenze r : stranka) { pripojIdTitulniFotky(r); }
            return stranka;
        }

        Long idPrihlasenehoUzivatele = (prihlaseny != null) ? prihlaseny.getIdUzivatele() : null;
        Page<Recenze> stranka;

        if (idPrihlasenehoUzivatele != null) {
            stranka = recenzeRepository.najdiViditelneRecenze(idPrihlasenehoUzivatele, pageable);
        } else {
            stranka = recenzeRepository.najdiVerejneRecenze(pageable);
        }

        for (Recenze r : stranka.getContent()) {
            pripojIdTitulniFotky(r);
        }
        return stranka;
    }

    public List<Recenze> ziskejViditelneRecenzeProPodnik(Long idPodniku, Uzivatel prihlaseny) {
        if (prihlaseny != null && (prihlaseny.getRole().getIdRole() == 2 || prihlaseny.getRole().getIdRole() == 3)) {
            List<Recenze> recenze = recenzeRepository.najdiVsechnyRecenzePodnikuProAdmina(idPodniku);
            for (Recenze r : recenze) {
                pripojIdTitulniFotky(r);
            }
            return recenze;
        }

        Long idPrihlasenehoUzivatele = (prihlaseny != null) ? prihlaseny.getIdUzivatele() : null;
        List<Recenze> recenze;

        if (idPrihlasenehoUzivatele != null) {
            recenze = recenzeRepository.najdiViditelneRecenzePodniku(idPodniku, idPrihlasenehoUzivatele);
        } else {
            recenze = recenzeRepository.najdiVerejneRecenzePodniku(idPodniku);
        }

        for (Recenze r : recenze) {
            pripojIdTitulniFotky(r);
        }
        return recenze;
    }

    private void pripojIdTitulniFotky(Recenze r) {
        List<Long> ids = fotkaRepository.najdiIdFotekPodleRecenze(r.getIdObsahu());
        if (!ids.isEmpty()) {
            r.setIdTitulniFotky(ids.get(0));
        }
    }

    public Page<Recenze> ziskejMojeRecenze(Pageable pageable, Long idPrihlasenehoUzivatele) {

        Page<Recenze> stranka = recenzeRepository.findByObsahUzivatelIdUzivateleOrderByIdObsahuDesc(idPrihlasenehoUzivatele, pageable);

        for (Recenze r : stranka.getContent()) {
            pripojIdTitulniFotky(r);
        }

        return stranka;
    }

    public void oznacJakoSmazanou(Long idRecenze) {
        Recenze r = recenzeRepository.findById(idRecenze).orElse(null);
        if (r != null) {

            Viditelnost smazanaViditelnost = new Viditelnost();

            smazanaViditelnost.setIdViditelnosti(5L);;

            r.getObsah().setViditelnost(smazanaViditelnost);

            recenzeRepository.save(r);
        }
    }

    public Page<Recenze> ziskejFiltrovaneMojeRecenze(Pageable pageable, Long idUzivatele, String business, List<String> tags) {

        Long typId = (business == null || business.equals("all")) ? null : Long.parseLong(business);

        Long tagCount = (tags == null || tags.isEmpty()) ? null : (long) tags.size();

        Page<Recenze> stranka = recenzeRepository.najdiMojeFiltrovaneRecenze(idUzivatele, typId, tags, tagCount, pageable);

        for (Recenze r : stranka.getContent()) {
            pripojIdTitulniFotky(r);
        }

        return stranka;
    }

    public List<Recenze> ziskejRecenzePodleUzivatele(Long idAutora, Uzivatel prihlaseny) {
        Long mojeId = (prihlaseny != null) ? prihlaseny.getIdUzivatele() : -1L;
        List<Recenze> seznam;

        if (prihlaseny != null && (prihlaseny.getRole().getIdRole() == 2 || prihlaseny.getRole().getIdRole() == 3)) {
            seznam = recenzeRepository.najdiVsechnyRecenzeUzivateleProAdmina(idAutora);
        } else {
            seznam = recenzeRepository.najdiViditelneRecenzeUzivatele(idAutora, mojeId);
        }

        for (Recenze r : seznam) {
            pripojIdTitulniFotky(r);
        }
        return seznam;
    }

    public Page<Recenze> ziskejGlobalniFiltrovaneRecenze(Pageable pageable, Uzivatel prihlaseny, String business, List<String> tags) {

        if (prihlaseny != null && (prihlaseny.getRole().getIdRole() == 2 || prihlaseny.getRole().getIdRole() == 3)) {
            Page<Recenze> stranka = recenzeRepository.najdiVsechnyRecenzeProAdmina(pageable);
            for (Recenze r : stranka) { pripojIdTitulniFotky(r); }
            return stranka;
        }

        Long mojeId = (prihlaseny != null) ? prihlaseny.getIdUzivatele() : -1L;
        Long typId = (business == null || business.equals("all")) ? null : Long.parseLong(business);
        Long tagCount = (tags == null || tags.isEmpty()) ? null : (long) tags.size();

        Page<Recenze> stranka = recenzeRepository.najdiGlobalniFiltrovaneRecenze(mojeId, typId, tags, tagCount, pageable);

        for (Recenze r : stranka.getContent()) {
            pripojIdTitulniFotky(r);
        }
        return stranka;
    }

    public void zmenViditelnost(Long idRecenze, Long idNoveViditelnosti) {
        Recenze r = recenzeRepository.findById(idRecenze).orElse(null);
        if (r != null) {
            Viditelnost novaViditelnost = new Viditelnost();
            novaViditelnost.setIdViditelnosti(idNoveViditelnosti);

            r.getObsah().setViditelnost(novaViditelnost);

            recenzeRepository.save(r);
        }
    }
}