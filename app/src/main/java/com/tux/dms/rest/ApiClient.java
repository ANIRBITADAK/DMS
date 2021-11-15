package com.tux.dms.rest;

import com.tux.dms.cache.SessionCache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    private static ApiInterface retrofitInterface;
    private static final String IP_ADDRESS = "143.244.131.27";
    private static final String PORT = "5000";
//    private static final String IP_ADDRESS = "10.0.2.2";
    private static String BASE_URL = "http://" + IP_ADDRESS + ":" + PORT;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiInterface getApiService() {
        return getRetrofitInstance().create(ApiInterface.class);
    }

    public static String getIpAddress() {
        return IP_ADDRESS;
    }

    public static String getPORT() {
        return PORT;
    }
}
