package com.kotkova.reviewed.model;
import jakarta.persistence.*;
@Entity
@Table(name = "KRAJE")
public class Kraj {

    @Id
    @Column(name = "id_kraje")
    private Long idKraje;

    @Column(name = "nazev")
    private String nazev;

    // --- GETTERY A SETTERY ---
    public Long getIdKraje() { return idKraje; }
    public void setIdKraje(Long idKraje) { this.idKraje = idKraje; }

    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }
}