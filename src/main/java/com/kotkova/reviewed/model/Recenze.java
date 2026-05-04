package com.kotkova.reviewed.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "RECENZE")
public class Recenze {

    @Id
    private Long idObsahu;

    @Column(name = "hodnoceni")
    private Integer hodnoceni;

    @Column(name = "datum_navstevy")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Pomáhá Springu zpracovat datum z formuláře
    private LocalDate datumNavstevy;

    @Column(name = "utrata")
    private Double utrata;

    @ManyToOne
    @JoinColumn(name = "id_podniku")
    private Podnik podnik;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId // Tato anotace je klíčová!
    @JoinColumn(name = "id_obsahu")
    private Obsah obsah = new Obsah();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recenze")
    private List<Fotka> fotky;

    @Transient
    private Long idTitulniFotky;

    // Vygeneruj si k tomu Getter a Setter:
    public Long getIdTitulniFotky() {
        return idTitulniFotky;
    }

    public void setIdTitulniFotky(Long idTitulniFotky) {
        this.idTitulniFotky = idTitulniFotky;
    }
    // Přidej Getter pro fotky
    public List<Fotka> getFotky() { return fotky; }

    // --- GETTERY A SETTERY ---

    public Long getIdObsahu() { return idObsahu; }
    public void setIdObsahu(Long idObsahu) { this.idObsahu = idObsahu; }

    public Podnik getPodnik() { return podnik; }
    public void setPodnik(Podnik podnik) { this.podnik = podnik; }

    public Integer getHodnoceni() { return hodnoceni; }
    public void setHodnoceni(Integer hodnoceni) { this.hodnoceni = hodnoceni; }

    public LocalDate getDatumNavstevy() { return datumNavstevy; }
    public void setDatumNavstevy(LocalDate datumNavstevy) { this.datumNavstevy = datumNavstevy; }

    public Double getUtrata() { return utrata; }
    public void setUtrata(Double utrata) { this.utrata = utrata; }

    public Obsah getObsah() { return obsah; }
    public void setObsah(Obsah obsah) { this.obsah = obsah; }


    // ... tvé stávající gettery a settery (např. getObsah)

    public String getJmenoProDivaka(Long idPrihlaseneho) {
        if (obsah.getViditelnost().getIdViditelnosti() == 1L) {
            // Je to anonym - ukážeme jméno jen autorovi
            if (obsah.getUzivatel().getIdUzivatele().equals(idPrihlaseneho)) {
                return obsah.getUzivatel().getPrezdivka();
            }
            return "Anonym";
        }
        // Veřejné a ostatní recenze - klasické jméno
        return obsah.getUzivatel().getPrezdivka();
    }

    public Long getAutorId() {
        if (obsah != null && obsah.getUzivatel() != null) {
            return obsah.getUzivatel().getIdUzivatele();
        }
        return null;
    }
}
