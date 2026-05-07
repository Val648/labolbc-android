package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

public class Specialite {
    @SerializedName(value = "id", alternate = {"numeroSequentiel"})
    private int id;
    
    private String libelle;

    public Specialite() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
}
