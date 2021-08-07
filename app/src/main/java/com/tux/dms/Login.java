package com.tux.dms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tux.dms.api.RetroInterface;
import com.tux.dms.dto.JWTToken;
import com.tux.dms.dto.UserCredential;
import com.tux.dms.restclient.RetroRestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    Button login,register;
    private RetroInterface retrofitInterface;
    EditText email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button) findViewById(R.id.buttonLogin);
        register=(Button) findViewById(R.id.buttonRegister);
        email=(EditText) findViewById(R.id.editTextEmail);
        password=(EditText) findViewById(R.id.editTextPass);


        Retrofit retrofitClient= RetroRestClient.getClient();
        retrofitInterface = retrofitClient.create(RetroInterface.class);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });

    }

    private void handleLogin() {

        /*HashMap<String, String> map = new HashMap<>();
        map.put("email", email.getText().toString());
        map.put("password", password.getText().toString());*/

        UserCredential credential = new UserCredential();
        credential.setEmail(email.getText().toString());
        credential.setPassword(password.getText().toString());
        Call<JWTToken> call = retrofitInterface.executeLogin(credential);

        call.enqueue( new Callback<JWTToken>() {
            @Override
            public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {

                if (response.code() == 200) {

                    JWTToken token = response.body();

                    Toast.makeText(getApplicationContext(),token.getToken(),Toast.LENGTH_LONG).show();

                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",
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