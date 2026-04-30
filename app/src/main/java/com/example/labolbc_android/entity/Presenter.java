package com.example.labolbc_android.entity;

import java.util.Date;

public class Presenter {
    private int idVisiteur;
    private int numRegion;
    private String idMedicament;
    private Date dateAffect;

    public Presenter() {}

    public Presenter(int idVisiteur, int numRegion, String idMedicament, Date dateAffect) {
        this.idVisiteur = idVisiteur;
        this.numRegion = numRegion;
        this.idMedicament = idMedicament;
        this.dateAffect = dateAffect;
    }

    public int getIdVisiteur() { return idVisiteur; }
    public void setIdVisiteur(int idVisiteur) { this.idVisiteur = idVisiteur; }

    public int getNumRegion() { return numRegion; }
    public void setNumRegion(int numRegion) { this.numRegion = numRegion; }

    public String getIdMedicament() { return idMedicament; }
    public void setIdMedicament(String idMedicament) { this.idMedicament = idMedicament; }

    public Date getDateAffect() { return dateAffect; }
    public void setDateAffect(Date dateAffect) { this.dateAffect = dateAffect; }
}
