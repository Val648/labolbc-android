package com.example.labolbc_android.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VisiteResponse {
    @SerializedName("visites")
    private List<Visite> visites;

    public List<Visite> getVisites() {
        return visites;
    }

    public void setVisites(List<Visite> visites) {
        this.visites = visites;
    }
}
