package com.kotkova.reviewed.model;

import java.io.Serializable;
import java.util.Objects;

public class UlozenyPodnikId implements Serializable {
    private Long uzivatel;
    private Long podnik;

    public UlozenyPodnikId() {}

    public UlozenyPodnikId(Long uzivatel, Long podnik) {
        this.uzivatel = uzivatel;
        this.podnik = podnik;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UlozenyPodnikId that = (UlozenyPodnikId) o;
        return Objects.equals(uzivatel, that.uzivatel) && Objects.equals(podnik, that.podnik);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uzivatel, podnik);
    }
}