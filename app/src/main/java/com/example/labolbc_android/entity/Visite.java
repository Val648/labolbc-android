package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Visite {
    private Integer id;
    
    @SerializedName(value = "date", alternate = {"dateVisite"})
    private Date dateVisite;
    
    @SerializedName(value = "motif", alternate = {"motifVisite"})
    private String motifVisite;
    
    @SerializedName(value = "bilan", alternate = {"bilanVisite"})
    private String bilanVisite;
    
    private String compteRendu;
    
    private Visiteur visiteur;
    private Praticien praticien;

    public Visite() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Date getDateVisite() { return dateVisite; }
    public void setDateVisite(Date dateVisite) { this.dateVisite = dateVisite; }

    public String getMotifVisite() { return motifVisite; }
    public void setMotifVisite(String motifVisite) { this.motifVisite = motifVisite; }

    public String getBilanVisite() { return bilanVisite; }
    public void setBilanVisite(String bilanVisite) { this.bilanVisite = bilanVisite; }

    public String getCompteRendu() { return compteRendu; }
    public void setCompteRendu(String compteRendu) { this.compteRendu = compteRendu; }

    public Visiteur getVisiteur() { return visiteur; }
    public void setVisiteur(Visiteur visiteur) { this.visiteur = visiteur; }

    public Praticien getPraticien() { return praticien; }
    public void setPraticien(Praticien praticien) { this.praticien = praticien; }

    public String getNomVisiteur() {
        return visiteur != null ? visiteur.getNomVisiteur() : "Inconnu";
    }

    public String getNomPraticien() {
        return praticien != null ? (praticien.getNomPraticien() + " " + praticien.getPrenomPraticien()) : "Inconnu";
    }
}
