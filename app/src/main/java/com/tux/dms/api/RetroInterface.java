package com.tux.dms.api;

import com.tux.dms.dto.JWTToken;

import com.tux.dms.dto.User;
import com.tux.dms.dto.UserCredential;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetroInterface {

    @POST("/api/auth")
    Call<JWTToken> executeLogin(@Body UserCredential credential);

    @POST("/api/users")
    Call<JWTToken> executeSignup (@Body User user);

}
