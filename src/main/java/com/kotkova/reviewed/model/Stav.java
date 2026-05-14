package com.kotkova.reviewed.model;

import jakarta.persistence.*;

@Entity
@Table(name = "STAVY")
public class Stav {

    @Id
    @Column(name = "id_stavu")
    private Long idStavu;

    @Column(name = "nazev")
    private String nazev;

    public Long getIdStavu() { return idStavu; }
    public void setIdStavu(Long idStavu) { this.idStavu = idStavu; }

    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }
}