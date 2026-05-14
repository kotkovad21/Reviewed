package com.kotkova.reviewed.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "STITKY")
public class Stitek {

    @Id
    @Column(name = "id_stitku")
    private Long idStitku;

    @Column(name = "nazev")
    private String nazev;

    @Transient
    private List<Podnik> podniky;

    public Long getIdStitku() { return idStitku; }
    public void setIdStitku(Long idStitku) { this.idStitku = idStitku; }
    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }
}