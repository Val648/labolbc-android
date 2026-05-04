package com.example.labolbc_android.entity;

public class Proposer {
    private int idVisite;
    private String idMedicament;
    private int nbEchantillon;

    public Proposer() {}

    public Proposer(int idVisite, String idMedicament, int nbEchantillon) {
        this.idVisite = idVisite;
        this.idMedicament = idMedicament;
        this.nbEchantillon = nbEchantillon;
    }

    public int getIdVisite() { return idVisite; }
    public void setIdVisite(int idVisite) { this.idVisite = idVisite; }

    public String getIdMedicament() { return idMedicament; }
    public void setIdMedicament(String idMedicament) { this.idMedicament = idMedicament; }

    public int getNbEchantillon() { return nbEchantillon; }
    public void setNbEchantillon(int nbEchantillon) { this.nbEchantillon = nbEchantillon; }
}
