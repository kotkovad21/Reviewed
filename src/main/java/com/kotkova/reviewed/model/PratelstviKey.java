package com.kotkova.reviewed.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PratelstviKey implements Serializable {

    @Column(name = "id_zadatele")
    private Long idZadatele;

    @Column(name = "id_prijemce")
    private Long idPrijemce;

    // Prázdný konstruktor pro Hibernate (nutnost)
    public PratelstviKey() {}

    public PratelstviKey(Long idZadatele, Long idPrijemce) {
        this.idZadatele = idZadatele;
        this.idPrijemce = idPrijemce;
    }

    public Long getIdZadatele() { return idZadatele; }
    public void setIdZadatele(Long idZadatele) { this.idZadatele = idZadatele; }

    public Long getIdPrijemce() { return idPrijemce; }
    public void setIdPrijemce(Long idPrijemce) { this.idPrijemce = idPrijemce; }

    // Pro složené klíče jsou tyto dvě metody naprosto nezbytné,
    // aby Java poznala, jestli jsou dvě přátelství stejná.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PratelstviKey that = (PratelstviKey) o;
        return Objects.equals(idZadatele, that.idZadatele) &&
                Objects.equals(idPrijemce, that.idPrijemce);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idZadatele, idPrijemce);
    }
}