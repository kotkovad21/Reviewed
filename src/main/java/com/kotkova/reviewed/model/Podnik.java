package com.kotkova.reviewed.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "PODNIKY") // Musí přesně odpovídat názvu tabulky v Oracle
public class Podnik {

    @Id // Označuje primární klíč
    @Column(name = "id_podniku")
    private Long idPodniku;

    @Column(name = "nazev")
    private String nazev;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "url_webu")
    private String urlWebu;

    @Column(name = "popis")
    private String popis;

    @Column(name = "oteviraci_doba")
    private String oteviraciDoba;

    // @ManyToOne znamená: "Více různých podniků může mít jeden stejný typ (např. vícero kaváren)"
    @ManyToOne
    // @JoinColumn říká Springu, podle jakého sloupečku to má v databázi spojit
    @JoinColumn(name = "id_typu_podniku")
    private TypPodniku typPodniku;

    @ManyToOne
    @JoinColumn(name = "id_adresy")
    private Adresa adresa;

    @Transient
    private List<Stitek> stitky;

    // mappedBy = "podnik" znamená: "Springu, podívej se do třídy Recenze na proměnnou 'podnik', tam zjistíš, jak se to má propojit."
    @OneToMany(mappedBy = "podnik")
    private List<Recenze> recenze;

    @Transient
    private Double prumernyRating;

    @Transient
    private Long idTitulniFotky;

    public Double getPrumernyRating() { return prumernyRating; }
    public void setPrumernyRating(Double prumernyRating) { this.prumernyRating = prumernyRating; }

    public Long getIdTitulniFotky() { return idTitulniFotky; }
    public void setIdTitulniFotky(Long idTitulniFotky) { this.idTitulniFotky = idTitulniFotky; }

    public Long getIdPodniku() { return idPodniku; }
    public void setIdPodniku(Long idPodniku) { this.idPodniku = idPodniku; }

    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getUrlWebu() { return urlWebu; }
    public void setUrlWebu(String urlWebu) { this.urlWebu = urlWebu; }

    public String getPopis() { return popis; }
    public void setPopis(String popis) { this.popis = popis; }

    public String getOteviraciDoba() { return oteviraciDoba; }
    public void setOteviraciDoba(String oteviraciDoba) { this.oteviraciDoba = oteviraciDoba; }

    public TypPodniku getTypPodniku() { return typPodniku; }
    public void setTypPodniku(TypPodniku typPodniku) { this.typPodniku = typPodniku; }

    public Adresa getAdresa() { return adresa; }
    public void setAdresa(Adresa adresa) { this.adresa = adresa; }

    public List<Stitek> getStitky() { return stitky; }
    public void setStitky(List<Stitek> stitky) { this.stitky = stitky; }

    public List<Recenze> getRecenze() { return recenze; }
    public void setRecenze(List<Recenze> recenze) { this.recenze = recenze; }


}