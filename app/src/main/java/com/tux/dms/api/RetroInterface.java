package com.tux.dms.api;

import com.tux.dms.dto.JWTToken;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetroInterface {

    @POST("/api/auth")
    Call<JWTToken> executeLogin(@Body HashMap<String,String> credential);

    @POST("/api/users")
    Call<JWTToken> executeSignup (@Body HashMap<String,String> user);

}
