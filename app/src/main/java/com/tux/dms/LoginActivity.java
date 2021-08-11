package com.tux.dms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.RoleConsts;
import com.tux.dms.rest.ApiInterface;
import com.tux.dms.dto.JWTToken;
import com.tux.dms.dto.User;
import com.tux.dms.dto.UserCredential;
import com.tux.dms.rest.ApiClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    Button login, register;
    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.buttonLogin);
        register = (Button) findViewById(R.id.buttonRegister);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLongin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    private void handleLongin() {
        getToken();
    }

    private void getToken() {

        EditText email = (EditText) findViewById(R.id.editTextEmail);
        EditText password = (EditText) findViewById(R.id.editTextPass);
        UserCredential user = new UserCredential();
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());

        Call<JWTToken> call = apiInterface.executeLogin(user);


        call.enqueue(new Callback<JWTToken>() {
            @Override
            public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {
                if (response.code() == 200) {

                    JWTToken token = response.body();
                    sessionCache.setToken(token.getToken());
                    getUser();

                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "Invalid credential",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JWTToken> call, Throwable t) {

            }
        });

    }

    private void getUser() {
        System.out.println("------called get user ");
        String token = sessionCache.getToken();
        Call<User> call = apiInterface.getUser(token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.code() == 200) {
                    User user = response.body();
                    sessionCache.setUser(user);
                    System.out.println("get users ===" + user.toString());
                    String loginMsg = user.getName() + " " + "logged in";
                    Toast.makeText(getApplicationContext(), loginMsg, Toast.LENGTH_LONG).show();

                    switch (user.getRole()){
                        case RoleConsts.ADMIN_ROLE:
                            Intent admin = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(admin);
                            break;
                        case RoleConsts.OPERATOR_ROLE:
                            System.out.println("user role "+ user.getRole());
                            Intent ticketOperatorIntent = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(ticketOperatorIntent);
                            break;
                        case RoleConsts.CREATOR_ROLE:
                            Intent ticketCreatorActivity = new Intent(LoginActivity.this, TicketCreatorActivity.class);
                            startActivity(ticketCreatorActivity);
                            break;
                        default:
                            System.out.println("invalid user role");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}