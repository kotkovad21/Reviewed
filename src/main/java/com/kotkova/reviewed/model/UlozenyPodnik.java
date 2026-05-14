package com.kotkova.reviewed.model;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "ULOZENE_PODNIKY")
@IdClass(UlozenyPodnikId.class)
public class UlozenyPodnik {

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_UZIVATELE")
    private Uzivatel uzivatel;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PODNIKU")
    private Podnik podnik;

    @Column(name = "DATUM_VYTVORENI", nullable = false)
    private LocalDate datumVytvoreni = LocalDate.now();


    public Uzivatel getUzivatel() {
        return uzivatel;
    }

    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

    public Podnik getPodnik() {
        return podnik;
    }

    public void setPodnik(Podnik podnik) {
        this.podnik = podnik;
    }

    public LocalDate getDatumVytvoreni() {
        return datumVytvoreni;
    }

    public void setDatumVytvoreni(LocalDate datumVytvoreni) {
        this.datumVytvoreni = datumVytvoreni;
    }
}