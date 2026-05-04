package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

public class Visiteur {
    private int id;

    @SerializedName("nom")
    private String nomVisiteur;

    public Visiteur() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomVisiteur() { return nomVisiteur; }
    public void setNomVisiteur(String nomVisiteur) { this.nomVisiteur = nomVisiteur; }
}
