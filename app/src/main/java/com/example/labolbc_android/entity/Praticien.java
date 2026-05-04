package com.example.labolbc_android.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Praticien {
    private int id;
    private int numeroSequentiel;
    private int idPraticien;

    @SerializedName("nom")
    private String nomPraticien;

    @SerializedName("prenom")
    private String prenomPraticien;

    private String specialite;

    public Praticien() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumeroSequentiel() { return numeroSequentiel; }
    public void setNumeroSequentiel(int numeroSequentiel) { this.numeroSequentiel = numeroSequentiel; }

    public int getIdPraticien() { return idPraticien; }
    public void setIdPraticien(int idPraticien) { this.idPraticien = idPraticien; }

    public String getNomPraticien() { return nomPraticien; }
    public void setNomPraticien(String nomPraticien) { this.nomPraticien = nomPraticien; }

    public String getPrenomPraticien() { return prenomPraticien; }
    public void setPrenomPraticien(String prenomPraticien) { this.prenomPraticien = prenomPraticien; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    @NonNull
    @Override
    public String toString() {
        return nomPraticien + " " + prenomPraticien;
    }
}
