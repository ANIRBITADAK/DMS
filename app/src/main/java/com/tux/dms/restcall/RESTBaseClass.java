package com.tux.dms.restcall;

import com.tux.dms.api.RetroInterface;
import com.tux.dms.restclient.RetroRestClient;

import retrofit2.Retrofit;

public class RESTBaseClass {

    protected RetroInterface retrofitInterface;

    public RESTBaseClass() {

        Retrofit retrofitClient= RetroRestClient.getClient();
        retrofitInterface = retrofitClient.create(RetroInterface.class);

    }




}
