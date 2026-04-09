package com.kotkova.reviewed.model;

import jakarta.persistence.*;

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

    // Záměrně zatím vynecháváme cizí klíče (id_typu_podniku, id_adresy, profilova_fotka).
    // Přidáme je, až nám bude fungovat tento základ!

    // --- GETTERY A SETTERY ---
    // Aby Spring mohl data číst a zapisovat, musíš vygenerovat gettery a settery.
    // V IntelliJ: Pravé tlačítko někam do kódu -> Generate -> Getter and Setter -> Vyber všechny parametry.

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
}