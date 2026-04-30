package com.example.labolbc_android.entity;

public class Visiteur {
    private int idVisiteur;
    private String nomVisiteur;

    public Visiteur() {}

    public Visiteur(int idVisiteur, String nomVisiteur) {
        this.idVisiteur = idVisiteur;
        this.nomVisiteur = nomVisiteur;
    }

    public int getIdVisiteur() { return idVisiteur; }
    public void setIdVisiteur(int idVisiteur) { this.idVisiteur = idVisiteur; }

    public String getNomVisiteur() { return nomVisiteur; }
    public void setNomVisiteur(String nomVisiteur) { this.nomVisiteur = nomVisiteur; }
}
