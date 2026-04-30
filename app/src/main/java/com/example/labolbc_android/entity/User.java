package com.example.labolbc_android.entity;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String statut;
    private String type;

    public User() {}

    public User(int id, String nom, String prenom, String email, String statut, String type) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.statut = statut;
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
