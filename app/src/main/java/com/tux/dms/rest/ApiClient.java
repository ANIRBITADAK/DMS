package com.tux.dms.rest;

import com.tux.dms.cache.SessionCache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    private static ApiInterface retrofitInterface;
    private static final String IP_ADDRESS = "3.111.159.79";
    private static final String PORT = "5000";
    //private static final String IP_ADDRESS = "10.0.2.2";
    private static String BASE_URL = "http://" + IP_ADDRESS + ":" + PORT;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
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
