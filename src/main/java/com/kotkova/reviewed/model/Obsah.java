package com.kotkova.reviewed.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OBSAHY")
public class Obsah {

    @Lob
    @Column(name = "text")
    private String text;

    @Column(name = "datum_vytvoreni")
    private LocalDate datumVytvoreni;

    @ManyToOne
    @JoinColumn(name = "id_uzivatele")
    private Uzivatel uzivatel;

    @ManyToMany
    @JoinTable(
            name = "OBSAHY_STITKY",
            joinColumns = @JoinColumn(name = "id_obsahu"),
            inverseJoinColumns = @JoinColumn(name = "id_stitku")
    )

    @OneToOne(mappedBy = "obsah")
    private Recenze recenze;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obsah_gen")
    @SequenceGenerator(name = "obsah_gen", sequenceName = "OBSAHY_ID_OBSAHU_SEQ", allocationSize = 1)
    @Column(name = "id_obsahu")
    private Long idObsahu;

    @ManyToMany
    @JoinTable(
            name = "OBSAHY_STITKY",
            joinColumns = @JoinColumn(name = "id_obsahu"),
            inverseJoinColumns = @JoinColumn(name = "id_stitku")
    )
    @OrderColumn(name = "PORADI")
    private List<Stitek> stitky = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_viditelnosti")
    private Viditelnost viditelnost;

    @Column(name = "typ_obsahu")
    private String typObsahu;

    public void setTypObsahu(String typObsahu) {
        this.typObsahu = typObsahu;
    }

    // Přidej Getter a Setter
    public Viditelnost getViditelnost() { return viditelnost; }
    public void setViditelnost(Viditelnost viditelnost) { this.viditelnost = viditelnost; }

    // Nezapomeň dolů přidat Getter a Setter!
    public List<Stitek> getStitky() { return stitky; }
    public void setStitky(List<Stitek> stitky) { this.stitky = stitky; }

    public Uzivatel getUzivatel() { return uzivatel; }
    public void setUzivatel(Uzivatel uzivatel) { this.uzivatel = uzivatel; }

    public Long getIdObsahu() { return idObsahu; }
    public void setIdObsahu(Long idObsahu) { this.idObsahu = idObsahu; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDate getDatumVytvoreni() { return datumVytvoreni; }
    public void setDatumVytvoreni(LocalDate datumVytvoreni) { this.datumVytvoreni = datumVytvoreni; }

    public void setRecenze(Recenze recenze) {
        this.recenze = recenze;
    }

}