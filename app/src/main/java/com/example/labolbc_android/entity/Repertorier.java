package com.example.labolbc_android.entity;

public class Repertorier {
    private int idVisiteur;
    private int idPraticien;

    public Repertorier() {}

    public Repertorier(int idVisiteur, int idPraticien) {
        this.idVisiteur = idVisiteur;
        this.idPraticien = idPraticien;
    }

    public int getIdVisiteur() { return idVisiteur; }
    public void setIdVisiteur(int idVisiteur) { this.idVisiteur = idVisiteur; }

    public int getIdPraticien() { return idPraticien; }
    public void setIdPraticien(int idPraticien) { this.idPraticien = idPraticien; }
}
