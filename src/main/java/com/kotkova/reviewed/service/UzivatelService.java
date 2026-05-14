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
        u.setDatumRegistrace(java.time.LocalDate.now());
        if (u.getKrestniJmeno() == null) u.setKrestniJmeno("Jméno");
        if (u.getPrijmeni() == null) u.setPrijmeni("Příjmení");

        Role defaultniRole = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Chyba: Role s ID 1 neexistuje v DB!"));
        Mesto defaultniMesto = mestoRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Chyba: Mesto s ID 1 neexistuje v DB!"));

        u.setRole(defaultniRole);
        u.setMesto(defaultniMesto);

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