package com.kotkova.reviewed.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PRATELSTVI")
public class Pratelstvi {

    @EmbeddedId
    private PratelstviKey id = new PratelstviKey();

    @Column(name = "datum_vzniku")
    private LocalDate datumVzniku;

    @ManyToOne
    @MapsId("idZadatele")
    @JoinColumn(name = "id_zadatele")
    private Uzivatel zadatel;

    @ManyToOne
    @MapsId("idPrijemce")
    @JoinColumn(name = "id_prijemce")
    private Uzivatel prijemce;

    @ManyToOne
    @JoinColumn(name = "id_stavu")
    private Stav stav;


    public PratelstviKey getId() { return id; }
    public void setId(PratelstviKey id) { this.id = id; }

    public LocalDate getDatumVzniku() { return datumVzniku; }
    public void setDatumVzniku(LocalDate datumVzniku) { this.datumVzniku = datumVzniku; }

    public Uzivatel getZadatel() { return zadatel; }
    public void setZadatel(Uzivatel zadatel) { this.zadatel = zadatel; }

    public Uzivatel getPrijemce() { return prijemce; }
    public void setPrijemce(Uzivatel prijemce) { this.prijemce = prijemce; }

    public Stav getStav() { return stav; }
    public void setStav(Stav stav) { this.stav = stav; }
}