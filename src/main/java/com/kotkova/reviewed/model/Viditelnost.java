package com.kotkova.reviewed.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TYPY_VIDITELNOSTI") // Zkontroluj přesný název v Oracle
public class Viditelnost {
    @Id
    @Column(name = "id_viditelnosti")
    private Long idViditelnosti;

    @Column(name = "nazev")
    private String nazev;

    public String getIkona() {

        // Tady rozhodujeme, jakou ikonu poslat podle ID v databázi
        switch (idViditelnosti.intValue()) {
            case 1: // Anonymní
            case 2: // Veřejné
                return "public";
            case 3: // Pouze přátelé
                return "group";
            case 4: // Pouze já
                return "lock";
            default:
                return "visibility";
        }
    }

    // Gettery a Settery
    public Long getIdViditelnosti() { return idViditelnosti; }
    public void setIdViditelnosti(Long idViditelnosti) { this.idViditelnosti = idViditelnosti; }
    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }
}