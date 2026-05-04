package com.example.labolbc_android.entity;

public class Region {
    private int numRegion;
    private String libelleRegion;

    public Region() {}

    public Region(int numRegion, String libelleRegion) {
        this.numRegion = numRegion;
        this.libelleRegion = libelleRegion;
    }

    public int getNumRegion() { return numRegion; }
    public void setNumRegion(int numRegion) { this.numRegion = numRegion; }

    public String getLibelleRegion() { return libelleRegion; }
    public void setLibelleRegion(String libelleRegion) { this.libelleRegion = libelleRegion; }
}
