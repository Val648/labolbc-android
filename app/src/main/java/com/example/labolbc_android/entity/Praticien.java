package com.example.labolbc_android.entity;

public class Praticien {
    private int idPraticien;
    private String nomPraticien;
    private String prenomPraticien;

    public Praticien() {}

    public Praticien(int idPraticien, String nomPraticien, String prenomPraticien) {
        this.idPraticien = idPraticien;
        this.nomPraticien = nomPraticien;
        this.prenomPraticien = prenomPraticien;
    }

    public int getIdPraticien() { return idPraticien; }
    public void setIdPraticien(int idPraticien) { this.idPraticien = idPraticien; }

    public String getNomPraticien() { return nomPraticien; }
    public void setNomPraticien(String nomPraticien) { this.nomPraticien = nomPraticien; }

    public String getPrenomPraticien() { return prenomPraticien; }
    public void setPrenomPraticien(String prenomPraticien) { this.prenomPraticien = prenomPraticien; }
}
