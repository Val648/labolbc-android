package com.example.labolbc_android.entity;

public class Visiteur {
    private int idVisiteur;

    private String nomVisiteur;

    public Visiteur() {}

    public int getId() { return idVisiteur; }
    public void setId(int id) { this.idVisiteur = id; }

    public String getNomVisiteur() { return nomVisiteur; }
    public void setNomVisiteur(String nomVisiteur) { this.nomVisiteur = nomVisiteur; }
}
