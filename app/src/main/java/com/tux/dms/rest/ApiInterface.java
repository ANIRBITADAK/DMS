package com.tux.dms.rest;

import com.tux.dms.dto.JWTToken;

import com.tux.dms.dto.ImageUploadResponse;
import com.tux.dms.dto.Ticket;
import com.tux.dms.dto.TicketList;
import com.tux.dms.dto.TicketCount;
import com.tux.dms.dto.User;
import com.tux.dms.dto.UserCredential;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {

    @POST("/api/auth")
    Call<JWTToken> executeLogin(@Body UserCredential credential);

    @POST("/api/users")
    Call<JWTToken> executeSignup(@Body User user);

    @GET("/api/auth")
    Call<User> getUser(@Header("x-auth-token") String authHeader);

    @POST("api/tickets")
    Call<Ticket> createTicket(@Header("x-auth-token") String authHeader, @Body Ticket ticket);

    @GET("api/tickets")
    Call<TicketList> getTickets(@Header("x-auth-token") String authHeader, @Query("assignedTo") String assignedTo, @Query("state") String state,
                                @Query("priority") Integer priority,
                                @Query("page") Integer page, @Query("limit") Integer limit);
    @GET("/api/tickets/count")
    Call<TicketCount> getTicketCount(@Header("x-auth-token") String authHeader, @Query("state") String state);

    @Multipart
    @POST("/api/images/upload")
    Call<ImageUploadResponse> uploadImage(@Part MultipartBody.Part image);
}
