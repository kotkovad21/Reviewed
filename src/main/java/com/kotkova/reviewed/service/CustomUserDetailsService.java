package com.kotkova.reviewed.service;

import com.kotkova.reviewed.model.Uzivatel;
import com.kotkova.reviewed.repository.UzivatelRepository;
import com.kotkova.reviewed.model.CustomUserDetails; // Nezapomeň na import!
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UzivatelRepository uzivatelRepository;

    public CustomUserDetailsService(UzivatelRepository uzivatelRepository) {
        this.uzivatelRepository = uzivatelRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Najdeme uživatele v databázi
        Uzivatel mujUzivatel = uzivatelRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Uživatel s emailem " + email + " nebyl nalezen."));

        // 2. TADY JE ZMĚNA: Vrátíme naši novou obálku, do které uživatele vložíme
        return new CustomUserDetails(mujUzivatel);
    }
}