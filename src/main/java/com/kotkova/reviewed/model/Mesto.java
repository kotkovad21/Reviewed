package com.kotkova.reviewed.model;
import jakarta.persistence.*;
@Entity
@Table(name = "MESTA")
public class Mesto {

    @Id
    @Column(name = "id_mesta")
    private Long idMesta;

    @Column(name = "nazev")
    private String nazev;

    // --- GETTERY A SETTERY ---
    public Long getIdMesta() { return idMesta; }
    public void setIdMesta(Long idMesta) { this.idMesta = idMesta; }

    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }
}