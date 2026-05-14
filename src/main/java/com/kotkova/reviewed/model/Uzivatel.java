package com.kotkova.reviewed.model;

import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "UZIVATELE")
public class Uzivatel {

    @Column(name = "krestni_jmeno")
    private String krestniJmeno;

    @Column(name = "prijmeni")
    private String prijmeni;

    @Column(name = "prezdivka")
    private String prezdivka;

    @Column(name = "email")
    private String email;

    @Column(name = "heslo")
    private String heslo;

    @Column(name = "datum_registrace")
    private LocalDate datumRegistrace;

    @Column(name = "popis_profilu")
    private String popisProfilu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MESTA", nullable = false)
    private Mesto mesto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ROLE", nullable = false)
    private Role role;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uzivatel_gen")
    @SequenceGenerator(name = "uzivatel_gen", sequenceName = "UZIVATELE_ID_UZIVATELE_SEQ", allocationSize = 1)
    @Column(name = "ID_UZIVATELE")
    private Long idUzivatele;

    public Long getIdUzivatele() { return idUzivatele; }
    public void setIdUzivatele(Long idUzivatele) { this.idUzivatele = idUzivatele; }

    public String getKrestniJmeno() { return krestniJmeno; }
    public void setKrestniJmeno(String krestniJmeno) { this.krestniJmeno = krestniJmeno; }

    public String getPrijmeni() { return prijmeni; }
    public void setPrijmeni(String prijmeni) { this.prijmeni = prijmeni; }

    public String getPrezdivka() { return prezdivka; }
    public void setPrezdivka(String prezdivka) { this.prezdivka = prezdivka; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHeslo() { return heslo; }
    public void setHeslo(String heslo) { this.heslo = heslo; }

    public LocalDate getDatumRegistrace() { return datumRegistrace; }
    public void setDatumRegistrace(LocalDate datumRegistrace) { this.datumRegistrace = datumRegistrace; }

    public String getPopisProfilu() { return popisProfilu; }
    public void setPopisProfilu(String popisProfilu) { this.popisProfilu = popisProfilu; }

    public Mesto getMesto() { return mesto; }
    public void setMesto(Mesto mesto) { this.mesto = mesto; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}