package com.tux.dms.rest;

import com.tux.dms.dto.JWTToken;

import com.tux.dms.dto.Ticket;
import com.tux.dms.dto.User;
import com.tux.dms.dto.UserCredential;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiInterface {

    @POST("/api/auth")
    Call<JWTToken> executeLogin(@Body UserCredential credential);

    @POST("/api/users")
    Call<JWTToken> executeSignup (@Body User user);

    @GET("/api/auth")
    Call<User> getUser(@Header("x-auth-token") String authHeader);

    @POST("api/tickets/user/{user_id}")
    Call<Ticket> createTicket(@Body Ticket ticket, @Path("user_id") String user_id);
}
