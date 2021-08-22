package com.tux.dms.rest;

import com.tux.dms.dto.JWTToken;

import com.tux.dms.dto.ImageUploadResponse;
import com.tux.dms.dto.Ticket;
import com.tux.dms.dto.TicketList;
import com.tux.dms.dto.TicketCount;
import com.tux.dms.dto.AssignTicket;
import com.tux.dms.dto.User;
import com.tux.dms.dto.UserCredential;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @POST("/api/auth")
    Call<JWTToken> executeLogin(@Body UserCredential credential);

    @POST("/api/users")
    Call<JWTToken> executeSignup(@Body User user);
    @PUT("api/users")
    Call<User> updateUser(@Header("x-auth-token") String authHeader, @Body User user);
    @GET("/api/users")
    Call<List<User>> getAllUser(@Header("x-auth-token") String authHeader);

    @GET("/api/auth")
    Call<User> getUser(@Header("x-auth-token") String authHeader);

    @POST("api/tickets")
    Call<Ticket> createTicket(@Header("x-auth-token") String authHeader, @Body Ticket ticket);

    @PUT("api/tickets/assign/{ticket_id}")
    Call<Ticket> assignTicket(@Header("x-auth-token") String authHeader, @Path("ticket_id") String ticketId,
                              @Body AssignTicket assignTicket);

    @POST("api/tickets/comment/{ticket_id}")
    Call<Ticket> commentTicket(@Header("x-auth-token") String authHeader, @Path("ticket_id") String ticketId,
                               @Body AssignTicket assignTicket);

    @GET("api/tickets")
    Call<TicketList> getTickets(@Header("x-auth-token") String authHeader,
                                @Query("assignedTo") String assignedTo, @Query("state") String state,
                                @Query("priority") String priority,
                                @Query("page") Integer page, @Query("limit") Integer limit);

    @GET("api/tickets/ticket_id/{ticket_id}")
    Call<Ticket> getTicket(@Header("x-auth-token") String authHeader, @Path("ticket_id") String ticketId);

    @GET("/api/tickets/count")
    Call<TicketCount> getTicketCount(@Header("x-auth-token") String authHeader, @Query("state") String state);
    @GET("/api/tickets/search")
    Call<TicketList> searchTicket(@Header("x-auth-token") String authHeader,
                                   @Query("subject") String subject,
                                   @Query("state") String state,
                                   @Query("priority") String priority,
                                   @Query("startDate") String startDate,
                                   @Query("endDate") String endDate,
                                   @Query("page") Integer page,
                                   @Query("limit") Integer limit);

    @Multipart
    @POST("/api/images/upload")
    Call<ImageUploadResponse> uploadImage(@Part MultipartBody.Part image);
}
