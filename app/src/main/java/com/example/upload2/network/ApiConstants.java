package com.example.upload2.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConstants {
    //==== Base Url
    public static String BASE_URL = "http://49.50.172.208:8080/";

    //==== End point
    public static final String ENDPOINT = "/Intuenty/staging/api/user/";

    //===== Retrofit Client
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
