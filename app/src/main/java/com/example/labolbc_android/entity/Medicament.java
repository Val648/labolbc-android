package com.example.labolbc_android.entity;

public class Medicament {
    private String idMedicament;
    private String libelleMedicament;

    public Medicament() {}

    public Medicament(String idMedicament, String libelleMedicament) {
        this.idMedicament = idMedicament;
        this.libelleMedicament = libelleMedicament;
    }

    public String getIdMedicament() { return idMedicament; }
    public void setIdMedicament(String idMedicament) { this.idMedicament = idMedicament; }

    public String getLibelleMedicament() { return libelleMedicament; }
    public void setLibelleMedicament(String libelleMedicament) { this.libelleMedicament = libelleMedicament; }
}
