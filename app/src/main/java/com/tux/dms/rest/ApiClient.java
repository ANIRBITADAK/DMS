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
    private static String BASE_URL = "http://143.244.131.27:5000";
    //private static SessionCache sessionCache = SessionCache.getSessionCache();


    public static Retrofit getRetrofitInstance() {

       /* OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String token = sessionCache.getToken();
                Response response = chain.proceed(chain.request());
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("x-auth-token", token)
                        .build();
                return chain.proceed(newRequest);

            }
        }).build();*/

        retrofit = new Retrofit.Builder()
                //.client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


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
}
