package com.kotkova.reviewed.model; // Uprav balíček podle sebe

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    // Zde si uložíme celého tvého uživatele z DB!
    private final Uzivatel uzivatel;

    public CustomUserDetails(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

    // --- TVOJE VLASTNÍ METODY ---

    // Tohle je ta hlavní pecka - kdykoliv si můžeš sáhnout pro ID
    public Long getIdUzivatele() {
        return uzivatel.getIdUzivatele();
    }

    // Nebo rovnou pro celého uživatele
    public Uzivatel getUzivatel() {
        return uzivatel;
    }

    // --- POVINNÉ METODY PRO SPRING SECURITY ---

    @Override
    public String getUsername() {
        return uzivatel.getEmail();
    }

    @Override
    public String getPassword() {
        return uzivatel.getHeslo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Ostatní bezpečnostní metody musí vracet true (jinak by se nešlo přihlásit)
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}