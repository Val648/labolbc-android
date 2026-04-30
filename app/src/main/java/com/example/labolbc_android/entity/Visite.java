package com.example.labolbc_android.entity;

import java.util.Date;

public class Visite {
    private int id;
    private Date dateVisite;
    private String motifVisite;
    private String bilanVisite;
    private int idVisiteur;
    private int idPraticien;

    public Visite() {}

    public Visite(int id, Date dateVisite, String motifVisite, String bilanVisite, int idVisiteur, int idPraticien) {
        this.id = id;
        this.dateVisite = dateVisite;
        this.motifVisite = motifVisite;
        this.bilanVisite = bilanVisite;
        this.idVisiteur = idVisiteur;
        this.idPraticien = idPraticien;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDateVisite() { return dateVisite; }
    public void setDateVisite(Date dateVisite) { this.dateVisite = dateVisite; }

    public String getMotifVisite() { return motifVisite; }
    public void setMotifVisite(String motifVisite) { this.motifVisite = motifVisite; }

    public String getBilanVisite() { return bilanVisite; }
    public void setBilanVisite(String bilanVisite) { this.bilanVisite = bilanVisite; }

    public int getIdVisiteur() { return idVisiteur; }
    public void setIdVisiteur(int idVisiteur) { this.idVisiteur = idVisiteur; }

    public int getIdPraticien() { return idPraticien; }
    public void setIdPraticien(int idPraticien) { this.idPraticien = idPraticien; }
}
