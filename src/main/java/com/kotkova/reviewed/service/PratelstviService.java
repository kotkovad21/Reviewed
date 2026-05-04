package com.kotkova.reviewed.service;

import com.kotkova.reviewed.model.Pratelstvi;
import com.kotkova.reviewed.model.PratelstviKey;
import com.kotkova.reviewed.model.Stav;
import com.kotkova.reviewed.model.Uzivatel;
import com.kotkova.reviewed.repository.PratelstviRepository;
import com.kotkova.reviewed.repository.UzivatelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PratelstviService {
    private final PratelstviRepository pratelstviRepository;
    private final UzivatelRepository uzivatelRepository;

    public PratelstviService(PratelstviRepository pratelstviRepository, UzivatelRepository uzivatelRepository) {
        this.pratelstviRepository = pratelstviRepository;
        this.uzivatelRepository = uzivatelRepository;
    }

    public List<Pratelstvi> ziskejZadosti(Long id) {
        return pratelstviRepository.findByPrijemceIdUzivateleAndStavIdStavu(id, 1L);
    }

    public List<Uzivatel> ziskejPratele(Long mojeId) {
        List<Pratelstvi> vazby = pratelstviRepository.najdiPotvrzenePratele(mojeId);
        // Převedeme vazby na seznam uživatelů (ten druhý člověk ve vazbě)
        return vazby.stream()
                .map(p -> p.getZadatel().getIdUzivatele().equals(mojeId) ? p.getPrijemce() : p.getZadatel())
                .toList();
    }

    public void prijmiZadost(Long zadatelId, Long prijemceId) {
        PratelstviKey key = new PratelstviKey(zadatelId, prijemceId);
        pratelstviRepository.findById(key).ifPresent(p -> {
            Stav prijato = new Stav();
            prijato.setIdStavu(2L);
            p.setStav(prijato);
            pratelstviRepository.save(p);
        });
    }

    public void smazPratelstvi(Long id1, Long id2) {
        // Zkusíme smazat obě kombinace (já-přítel i přítel-já)
        pratelstviRepository.deleteById(new PratelstviKey(id1, id2));
        pratelstviRepository.deleteById(new PratelstviKey(id2, id1));
    }

    // Metoda pro odeslání nové žádosti o přátelství
    public void posliZadost(Long zadatelId, Long prijemceId) {
        // Kontrola, zda vazba už neexistuje v nějakém směru
        if (zjistiStavVazby(zadatelId, prijemceId) != null) {
            return; // Vazba už existuje, nic neděláme
        }

        Uzivatel zadatel = uzivatelRepository.findById(zadatelId).orElse(null);
        Uzivatel prijemce = uzivatelRepository.findById(prijemceId).orElse(null);

        if (zadatel != null && prijemce != null) {
            Pratelstvi zadost = new Pratelstvi();
            zadost.setId(new PratelstviKey(zadatelId, prijemceId));
            zadost.setZadatel(zadatel);
            zadost.setPrijemce(prijemce);
            zadost.setDatumVzniku(java.time.LocalDate.now());

            Stav ceka = new Stav();
            ceka.setIdStavu(1L); // 1 = Čekající žádost
            zadost.setStav(ceka);

            pratelstviRepository.save(zadost);
        }
    }

    // Pomocná metoda pro zjištění, jestli se ti dva už neznají
    public String zjistiStavVazby(Long id1, Long id2) {
        // Kontrola, jestli jsem poslal já jemu
        var vazba1 = pratelstviRepository.findById(new PratelstviKey(id1, id2));
        if (vazba1.isPresent()) {
            return vazba1.get().getStav().getIdStavu() == 2L ? "PRIJATO" : "ZADOST";
        }

        // Kontrola, jestli poslal on mně
        var vazba2 = pratelstviRepository.findById(new PratelstviKey(id2, id1));
        if (vazba2.isPresent()) {
            return vazba2.get().getStav().getIdStavu() == 2L ? "PRIJATO" : "ZADOST";
        }

        return null; // Žádná vazba
    }
}
