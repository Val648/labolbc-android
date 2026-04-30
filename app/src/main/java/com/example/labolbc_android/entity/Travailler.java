package com.example.labolbc_android.entity;

import java.util.Date;

public class Travailler {
    private int idPraticien;
    private int numRegion;
    private Date dateA;

    public Travailler() {}

    public Travailler(int idPraticien, int numRegion, Date dateA) {
        this.idPraticien = idPraticien;
        this.numRegion = numRegion;
        this.dateA = dateA;
    }

    public int getIdPraticien() { return idPraticien; }
    public void setIdPraticien(int idPraticien) { this.idPraticien = idPraticien; }

    public int getNumRegion() { return numRegion; }
    public void setNumRegion(int numRegion) { this.numRegion = numRegion; }

    public Date getDateA() { return dateA; }
    public void setDateA(Date dateA) { this.dateA = dateA; }
}
