package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

public class Specialite {
    public int numeroSequentiel;
    public String libelle;

    public Specialite() {}

    public Specialite(int numeroSequentiel, String libelle) {
        this.numeroSequentiel = numeroSequentiel;
        this.libelle = libelle;
    }

    public int getNumeroSequentiel() { return numeroSequentiel; }
    public void setNumeroSequentiel(int id) { this.numeroSequentiel = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
}
