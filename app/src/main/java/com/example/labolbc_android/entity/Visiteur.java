package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

public class Visiteur {
    @SerializedName("idVisiteur")
    private int idVisiteur;
    
    @SerializedName("nom")
    private String nomVisiteur;

    public Visiteur() {}

    public int getId() { return idVisiteur; }
    public void setId(int id) { this.idVisiteur = id; }

    public String getNomVisiteur() { return nomVisiteur; }
    public void setNomVisiteur(String nomVisiteur) { this.nomVisiteur = nomVisiteur; }
}
