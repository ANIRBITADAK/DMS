package com.tux.dms.restclient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroRestClient {

    private static Retrofit retrofit;
    private static String BASE_URL = "http://10.0.2.2:5000";

    public static Retrofit getClient()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }
}
