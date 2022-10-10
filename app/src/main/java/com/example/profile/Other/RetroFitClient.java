package com.example.profile.Other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroFitClient {
    private static RetroFitClient instance = null;
    private static Retrofit retrofit;
    private static String BASE_URL = "https://upgradeinfotech.in/projects/ModesToolbox/v1.1/Profile/";
//    private static String BASE_URL = "https://upgradeinfotech.in/projects/ModesToolbox/v1.1/";


    public static Retrofit getRetrofitInstance() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
