package com.example.labolbc_android.entity;

import androidx.annotation.NonNull;

public class Praticien {
    public int idPraticien;
    public String nom;
    public String prenom;
    public Specialite specialite;

    public Praticien() {}

    public int getIdPraticien() { return idPraticien; }
    public void setIdPraticien(int idPraticien) { this.idPraticien = idPraticien; }

    public String getNomPraticien() { return nom; }
    public void setNomPraticien(String nomPraticien) { this.nom = nomPraticien; }

    public String getPrenomPraticien() { return prenom; }
    public void setPrenomPraticien(String prenomPraticien) { this.prenom = prenomPraticien; }

    public Specialite getSpecialite() { return specialite; }
    public void setSpecialite(Specialite specialite) { this.specialite = specialite; }

    @NonNull
    @Override
    public String toString() {
        return (nom != null ? nom : "") + " " + (prenom != null ? prenom : "");
    }
}
