package com.example.labolbc_android.entity;

import java.util.Date;

public class Visite {
    private int idVisite;

    private Date dateVisite;

    private String motifVisite;

    private String bilanVisite;

    private String compteRenduVisite; // Lien vers le PDF

    private Visiteur visiteur;
    private Praticien praticien;

    public Visite() {}

    public Visite(int i, Date date, String visiteDeRoutine, String testBilan, String s, String jeanDupont, String s1) {
    }

    public int getId() { return idVisite; }
    public void setId(int id) { this.idVisite = id; }

    public Date getDateVisite() { return dateVisite; }
    public void setDateVisite(Date dateVisite) { this.dateVisite = dateVisite; }

    public String getMotifVisite() { return motifVisite; }
    public void setMotifVisite(String motifVisite) { this.motifVisite = motifVisite; }

    public String getBilanVisite() { return bilanVisite; }
    public void setBilanVisite(String bilanVisite) { this.bilanVisite = bilanVisite; }

    public String getCompteRendu() { return compteRenduVisite; }
    public void setCompteRendu(String compteRenduVisite) { this.compteRenduVisite = compteRenduVisite; }

    public Visiteur getVisiteur() { return visiteur; }
    public void setVisiteur(Visiteur visiteur) { this.visiteur = visiteur; }

    public Praticien getPraticien() { return praticien; }
    public void setPraticien(Praticien praticien) { this.praticien = praticien; }

    public String getNomVisiteur() {
        return visiteur != null ? visiteur.getNomVisiteur() : "Inconnu";
    }

    public String getNomPraticien() {
        if (praticien != null) {
            return (praticien.nomPraticien != null ? praticien.nomPraticien : "") + " " + (praticien.prenomPraticien != null ? praticien.prenomPraticien : "");
        }
        return "Inconnu";
    }
}
