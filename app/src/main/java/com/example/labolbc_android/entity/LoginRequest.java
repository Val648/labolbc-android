package com.example.labolbc_android.entity;

public class LoginRequest {
    private String email;
    private String password;
    private String device;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.device = "visiteur"; // Vous pouvez ajuster selon vos besoins
    }
}
