package com.tux.dms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tux.dms.cache.SessionCache;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;
import com.tux.dms.dto.JWTToken;
import com.tux.dms.dto.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    EditText name, password,email,phone;
    TextView signInRedirect;
    private ApiInterface retrofitInterface = ApiClient.getApiService();
    private SessionCache sessionCache = SessionCache.getSessionCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register=(Button) findViewById(R.id.registerButton);
        name=(EditText) findViewById(R.id.nameEditText);
        password=(EditText) findViewById(R.id.passwordEditText);
        email=(EditText) findViewById(R.id.emailEditText);
        phone=(EditText) findViewById(R.id.mobileNumberEditText);
        signInRedirect=(TextView)findViewById(R.id.signInRedirect);

    /*    Retrofit retrofitClient= RetroRestClient.getClient();
        retrofitInterface = retrofitClient.create(RetroInterface.class);*/

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               handleSignup();
            }
        });

        signInRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private void handleSignup() {

        User userObj=new User();
        userObj.setEmail(email.getText().toString());
        userObj.setPassword(password.getText().toString());
        userObj.setName(name.getText().toString());
        userObj.setPhone(phone.getText().toString());

        Call<JWTToken> call = retrofitInterface.executeSignup(userObj);

        call.enqueue(new Callback<JWTToken>() {
            @Override
            public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {

                if (response.code() == 200) {

                    JWTToken token = response.body();
                    sessionCache.setToken(token.getToken());
                    Toast.makeText(getApplicationContext(), "successfully registered", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "User Already Exists",
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
}