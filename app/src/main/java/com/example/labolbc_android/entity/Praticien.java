package com.example.labolbc_android.entity;

import androidx.annotation.NonNull;

public class Praticien {
    public int idPraticien;
    public String nomPraticien;
    public String prenomPraticien;
    public Specialite specialitePraticien;

    public Praticien() {}

    public int getIdPraticien() { return idPraticien; }
    public void setIdPraticien(int idPraticien) { this.idPraticien = idPraticien; }

    public String getNomPraticien() { return nomPraticien; }
    public void setNomPraticien(String nomPraticien) { this.nomPraticien = nomPraticien; }

    public String getPrenomPraticien() { return prenomPraticien; }
    public void setPrenomPraticien(String prenomPraticien) { this.prenomPraticien = prenomPraticien; }

    public Specialite getSpecialite() { return specialitePraticien; }
    public void setSpecialite(Specialite specialite) { this.specialitePraticien = specialite; }

    @NonNull
    @Override
    public String toString() {
        return (nomPraticien != null ? nomPraticien : "") + " " + (prenomPraticien != null ? prenomPraticien : "");
    }
}
