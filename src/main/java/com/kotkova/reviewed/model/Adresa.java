package com.kotkova.reviewed.model;
import jakarta.persistence.*;
@Entity
@Table(name = "ADRESY")
public class Adresa {

    @Id
    @Column(name = "id_adresy")
    private Long idAdresy;

    @Column(name = "ulice")
    private String ulice;

    @Column(name = "cislo_popisne")
    private String cisloPopisne;

    @Column(name = "psc")
    private String psc;

    @ManyToOne
    @JoinColumn(name = "id_mesta") // Toto jméno sloupce musí odpovídat tvé Oracle tabulce
    private Mesto mesto;

    // --- GETTERY A SETTERY ---
    public Long getIdAdresy() { return idAdresy; }
    public void setIdAdresy(Long idAdresy) { this.idAdresy = idAdresy; }

    public String getUlice() { return ulice; }
    public void setUlice(String ulice) { this.ulice = ulice; }

    public String getCisloPopisne() { return cisloPopisne; }
    public void setCisloPopisne(String cisloPopisne) { this.cisloPopisne = cisloPopisne; }

    public String getPsc() { return psc; }
    public void setPsc(String psc) { this.psc = psc; }

    public Mesto getMesto() { return mesto; }
    public void setMesto(Mesto mesto) { this.mesto = mesto; }
}