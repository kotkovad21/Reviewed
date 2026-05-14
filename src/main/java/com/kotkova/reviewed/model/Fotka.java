package com.kotkova.reviewed.model;

import jakarta.persistence.*;

@Entity
@Table(name = "FOTKY")
public class Fotka {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fotka_gen")
    @SequenceGenerator(name = "fotka_gen", sequenceName = "FOTKY_ID_FOTKY_SEQ", allocationSize = 1)
    @Column(name = "ID_FOTKY")
    private Long idFotky;

    @Column(name = "ID_RECENZE")
    private Long idRecenze;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "FOTKA")
    private byte[] data;

    @Column(name = "NAZEV_SOUBORU")
    private String nazevSouboru;

    public Long getIdFotky() {
        return idFotky;
    }

    public void setIdFotky(Long idFotky) {
        this.idFotky = idFotky;
    }

    public Long getIdRecenze() {
        return idRecenze;
    }

    public void setIdRecenze(Long idRecenze) {this.idRecenze = idRecenze;}

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
    public String getNazevSouboru() {return nazevSouboru;}

    public void setNazevSouboru(String nazevSouboru) {
        this.nazevSouboru = nazevSouboru;
    }
}