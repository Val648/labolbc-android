package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("nom")
    private String nom;
    
    @SerializedName("prenom")
    private String prenom;
    
    private String email;
    private String type;

    public int getId() { return id; }
    public String getName() { 
        if (name != null) return name;
        if (nom != null && prenom != null) return nom + " " + prenom;
        if (nom != null) return nom;
        return "Utilisateur";
    }
    public String getEmail() { return email; }
    public String getType() { return type; }
}
