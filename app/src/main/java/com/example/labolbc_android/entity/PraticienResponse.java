package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PraticienResponse {
    @SerializedName("praticiens")
    private List<Praticien> praticiens;

    public List<Praticien> getPraticiens() {
        return praticiens;
    }

    public void setPraticiens(List<Praticien> praticiens) {
        this.praticiens = praticiens;
    }
}
