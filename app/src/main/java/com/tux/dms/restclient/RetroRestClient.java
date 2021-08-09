package com.tux.dms.restclient;

import com.tux.dms.api.RestInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroRestClient {

    private static Retrofit retrofit;
    private static RestInterface retrofitInterface;
    private static String BASE_URL = "http://10.0.2.2:5000";

  /*  OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = chain.request().headers()
            Response response = chain.proceed(chain.request());
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("x-auth-token", token)
                    .build();
            return chain.proceed(newRequest);

        }
    }).build();*/

    public static RestInterface getRetroInterface() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        retrofitInterface = retrofit.create(RestInterface.class);
        return retrofitInterface;
    }
}
