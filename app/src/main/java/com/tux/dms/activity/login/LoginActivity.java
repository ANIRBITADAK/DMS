package com.tux.dms.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tux.dms.activity.admin.AdminActivity;
import com.tux.dms.R;
import com.tux.dms.activity.register.RegisterActivity;
import com.tux.dms.activity.creator.TicketCreatorActivity;
import com.tux.dms.activity.operator.UserActivity;
import com.tux.dms.cache.SessionCache;
import com.tux.dms.constants.RoleConstants;
import com.tux.dms.rest.ApiInterface;
import com.tux.dms.dto.JWTToken;
import com.tux.dms.dto.User;
import com.tux.dms.dto.UserCredential;
import com.tux.dms.rest.ApiClient;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView register;
    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.LoginButton);
        register = (TextView) findViewById(R.id.signupRedirect);


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

        EditText phoneNo = (EditText) findViewById(R.id.editTextPhone);
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        UserCredential user = new UserCredential();
        user.setPassword(password.getText().toString());
        user.setPhone(phoneNo.getText().toString());
        Call<JWTToken> call = apiInterface.executeLogin(user);


        call.enqueue(new Callback<JWTToken>() {
            @Override
            public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {
                if (response.code() == 200) {
                    JWTToken token = response.body();
                    sessionCache.setToken(token.getToken());
                    getUser();
                } else if (response.code() == 400) {
                    String errorMsg = getErrorMessage(response.errorBody());
                    Toast.makeText(getApplicationContext(), errorMsg,
                            Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<JWTToken> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getErrorMessage(ResponseBody errResponseBody) {
        String errorMsg = "Invalid credential";
        try {
            String errorBody = errResponseBody.string();
            JSONObject jsonObject = new JSONObject(errorBody.trim());
            JSONArray obj1 = (JSONArray) jsonObject.get("errors");
            JSONObject ob2 = (JSONObject) obj1.get(0);
            errorMsg = (String) ob2.get("msg");

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return errorMsg;
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
                        case RoleConstants.ADMIN_ROLE:
                            Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(adminIntent);
                            break;

                        case RoleConstants.OPERATOR_ROLE:
                            System.out.println("user role "+ user.getRole());
                            Intent ticketOperatorIntent = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(ticketOperatorIntent);
                            break;

                        case RoleConstants.CREATOR_ROLE:
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
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}