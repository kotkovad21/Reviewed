package com.kotkova.reviewed.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Uzivatel uzivatel;

    public CustomUserDetails(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }


    public Long getIdUzivatele() {
        return uzivatel.getIdUzivatele();
    }

    public Uzivatel getUzivatel() {
        return uzivatel;
    }

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

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}