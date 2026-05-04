package com.kotkova.reviewed.model;


import jakarta.persistence.*;

@Entity
    @Table(name = "ROLE")
    public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID_ROLE")
        private Long idRole;

        @Column(name = "NAZEV", nullable = false)
        private String nazevRole;

        // Gettery a settery
        public Long getIdRole() { return idRole; }
        public void setIdRole(Long idRole) { this.idRole = idRole; }
        public String getNazevRole() { return nazevRole; }
        public void setNazevRole(String nazevRole) { this.nazevRole = nazevRole; }
    }
