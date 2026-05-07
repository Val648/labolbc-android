package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

public class Visiteur {
    @SerializedName("idVisiteur")
    private int idVisiteur;
    
    @SerializedName("nom")
    private String nomVisiteur;

    public Visiteur() {}

    public int getIdVisiteur() { return idVisiteur; }
    public void setIdVisiteur(int idVisiteur) { this.idVisiteur = idVisiteur; }

    public String getNomVisiteur() { return nomVisiteur; }
    public void setNomVisiteur(String nomVisiteur) { this.nomVisiteur = nomVisiteur; }
}
