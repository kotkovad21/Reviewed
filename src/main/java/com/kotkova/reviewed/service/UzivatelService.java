package com.kotkova.reviewed.service;
import com.kotkova.reviewed.model.Mesto;
import com.kotkova.reviewed.model.Role;
import com.kotkova.reviewed.model.Uzivatel;
import com.kotkova.reviewed.repository.MestoRepository;
import com.kotkova.reviewed.repository.RoleRepository;
import com.kotkova.reviewed.repository.UzivatelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UzivatelService {
    private final UzivatelRepository uzivatelRepository;
    private final RoleRepository roleRepository;
    private final MestoRepository mestoRepository;

    // Všechny závislosti v jednom konstruktoru (Spring je tam doplní sám)
    public UzivatelService(UzivatelRepository uzivatelRepository,
                           RoleRepository roleRepository,
                           MestoRepository mestoRepository) {
        this.uzivatelRepository = uzivatelRepository;
        this.roleRepository = roleRepository;
        this.mestoRepository = mestoRepository;
    }

    public Uzivatel ziskejUzivatelePodleId(Long id) {
        return uzivatelRepository.findById(id).orElse(null);
    }

    public Uzivatel ziskejUzivatelePodleEmailu(String email) {
        return uzivatelRepository.findByEmail(email).orElse(null);
    }

    public void registrujNovehoUzivatele(Uzivatel u) {
        // 1. Nastavíme dnešní datum registrace
        u.setDatumRegistrace(java.time.LocalDate.now());
        // 2. Nastavíme defaultní hodnoty pro textová pole (No Nullable v DB)
        if (u.getKrestniJmeno() == null) u.setKrestniJmeno("Jméno");
        if (u.getPrijmeni() == null) u.setPrijmeni("Příjmení");

        // 3. Najdeme v DB město a roli s ID 1 (předpokládáme, že tam jsou)
        // .orElseThrow vyhodí chybu, pokud ID 1 v tabulce MESTO/ROLE neexistuje
        Role defaultniRole = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Chyba: Role s ID 1 neexistuje v DB!"));
        Mesto defaultniMesto = mestoRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Chyba: Mesto s ID 1 neexistuje v DB!"));

        u.setRole(defaultniRole);
        u.setMesto(defaultniMesto);

        // 4. Uložíme uživatele
        uzivatelRepository.save(u);
    }

    public List<Uzivatel> hledejUzivatele(String query) {
        return uzivatelRepository.findByPrezdivkaContainingIgnoreCase(query);
    }

    public boolean existujeEmail(String email) {
        return uzivatelRepository.existsByEmail(email);
    }

    public boolean existujePrezdivka(String prezdivka) {
        return uzivatelRepository.existsByPrezdivka(prezdivka);
    }
}