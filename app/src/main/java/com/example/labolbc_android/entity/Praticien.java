package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

public class Praticien {
    private int id;
    
    @SerializedName("nom")
    private String nomPraticien;
    
    @SerializedName("prenom")
    private String prenomPraticien;

    public Praticien() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomPraticien() { return nomPraticien; }
    public void setNomPraticien(String nomPraticien) { this.nomPraticien = nomPraticien; }

    public String getPrenomPraticien() { return prenomPraticien; }
    public void setPrenomPraticien(String prenomPraticien) { this.prenomPraticien = prenomPraticien; }
}
