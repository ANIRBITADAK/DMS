package com.tux.dms.restcall;

import com.tux.dms.api.RestInterface;

import retrofit2.Retrofit;

public class RESTBaseClass {

    protected RestInterface retrofitInterface;
    protected Retrofit retrofitClient;

    /*public RESTBaseClass() {
        retrofitClient = RetroRestClient.getClient();
        retrofitInterface = retrofitClient.create(RetroInterface.class);
    }*/
}
