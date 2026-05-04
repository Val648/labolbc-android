package com.example.labolbc_android.entity;

import java.util.Date;

public class Visite {
    private int id;
    private Date dateVisite;
    private String motifVisite;
    private String bilanVisite;
    private String nomVisiteur;
    private String nomPraticien;

    public Visite() {}

    public Visite(int id, Date dateVisite, String motifVisite, String bilanVisite, String nomVisiteur, String nomPraticien) {
        this.id = id;
        this.dateVisite = dateVisite;
        this.motifVisite = motifVisite;
        this.bilanVisite = bilanVisite;
        this.nomVisiteur = nomVisiteur;
        this.nomPraticien = nomPraticien;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDateVisite() { return dateVisite; }
    public void setDateVisite(Date dateVisite) { this.dateVisite = dateVisite; }

    public String getMotifVisite() { return motifVisite; }
    public void setMotifVisite(String motifVisite) { this.motifVisite = motifVisite; }

    public String getBilanVisite() { return bilanVisite; }
    public void setBilanVisite(String bilanVisite) { this.bilanVisite = bilanVisite; }

    public String getNomVisiteur() { return nomVisiteur; }
    public void setNomVisiteur(String nomVisiteur) { this.nomVisiteur = nomVisiteur; }

    public String getNomPraticien() { return nomPraticien; }
    public void setNomPraticien(String nomPraticien) { this.nomPraticien = nomPraticien; }
}
