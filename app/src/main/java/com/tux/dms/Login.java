package com.tux.dms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tux.dms.api.RestInterface;
import com.tux.dms.dto.JWTToken;
import com.tux.dms.dto.User;
import com.tux.dms.dto.UserCredential;
import com.tux.dms.restclient.RetroRestClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    Button login, register;
    RestInterface restInterface = RetroRestClient.getRetroInterface();
    String token ;
    User usr;
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
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

    }

    private  void handleLongin(){
        getToken();
       getUser(token);
        Log.d("TAG",  usr.getName());
    }
    private void getToken(){

        EditText email = (EditText) findViewById(R.id.editTextEmail);
        EditText password = (EditText) findViewById(R.id.editTextPass);
        UserCredential user=new UserCredential();
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());
        HashMap<String, String> map = new HashMap<>();

        Call<JWTToken> call = restInterface.executeLogin(user);


        call.enqueue( new Callback<JWTToken>() {
            @Override
            public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {
                Log.i("tag", "getting response of login");
                JWTToken token = response.body();
                setToken(token.getToken());
            }

            @Override
            public void onFailure(Call<JWTToken> call, Throwable t) {

            }
        });

    }

    private void getUser(String token){

        Call<User> call = restInterface.getUser(token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
               User user =  response.body();
               setUser(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    private  void setToken(String token){
        this.token = token;
    }
    private void setUser(User user){

    }
}