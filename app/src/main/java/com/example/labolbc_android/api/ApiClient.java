package com.example.labolbc_android.api;

import android.content.Context;
import com.example.labolbc_android.BuildConfig;
import com.example.labolbc_android.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static ApiService getService(Context context) {
        SessionManager sessionManager = new SessionManager(context);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder();

                    String token = sessionManager.getToken();
                    if (token != null) {
                        requestBuilder.header("Authorization", "Bearer " + token);
                    }

                    return chain.proceed(requestBuilder.build());
                })
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ApiService.class);
    }
}
