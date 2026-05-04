package com.example.labolbc_android.api;

import com.example.labolbc_android.entity.LoginRequest;
import com.example.labolbc_android.entity.LoginResponse;
import com.example.labolbc_android.entity.RegisterRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
//    @GET("/visites")
//    Call<List<Visite>> getVisites();
//
//    @GET("/visites/{id}")
//    Call<Visite> getVisite(@Path("id") int id);
//
//    @GET("/visiteur/visites")
//    Call<List<Visite>> getVisitesVisiteur();
//
//    @GET("/visiteur/visites/{id}")
//    Call<Visite> getVisiteVisiteur(@Path("id") int id);
//
//    @POST("/visiteur/visites")
//    Call<List<Visite>> createVisite(@Body Visite visite);
//
//    @GET("/visiteur/visites/{id}/report")
//    Call<List<Visite>> createCompteRendu(@Path("id") int id, @Body Visite visite);
//
//    @PUT("/visiteur/visites/{id}")
//    Call<List<Visite>> updateVisite(@Path("id") int id, @Body Visite visite);
//
//    @DELETE("/visiteur/visites/{id}")
//    Call<List<Visite>> deleteVisite(@Path("id") int id);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Call<LoginResponse> register(@Body RegisterRequest request);
}