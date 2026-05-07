package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

public class Praticien {
    
    @SerializedName("idPraticien")
    private int idPraticien;
    
    @SerializedName("nom")
    private String nomPraticien;
    
    @SerializedName("prenom")
    private String prenomPraticien;
    
    @SerializedName("specialite")
    private Specialite specialite;

    public Praticien() {}

    public int getIdPraticien() { return idPraticien; }
    public void setIdPraticien(int idPraticien) { this.idPraticien = idPraticien; }

    public String getNomPraticien() { return nomPraticien; }
    public void setNomPraticien(String nomPraticien) { this.nomPraticien = nomPraticien; }

    public String getPrenomPraticien() { return prenomPraticien; }
    public void setPrenomPraticien(String prenomPraticien) { this.prenomPraticien = prenomPraticien; }

    public Specialite getSpecialite() { return specialite; }
    public void setSpecialite(Specialite specialite) { this.specialite = specialite; }

    public String getSpecialiteLabel() {
        return specialite != null ? specialite.getLibelle() : "";
    }
}
