package com.tux.dms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tux.dms.constantkeys.RESTApiConstant;
import com.tux.dms.dto.JWTToken;
import com.tux.dms.restcall.LoginUser;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    Button login,register;

    EditText email,password;
    LoginUser loginUser=new LoginUser();
    Map<String,String> map=new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button) findViewById(R.id.buttonLogin);
        register=(Button) findViewById(R.id.buttonRegister);
        email=(EditText) findViewById(R.id.editTextEmail);
        password=(EditText) findViewById(R.id.editTextPass);





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 map=loginUser.handleLogin(email.getText().toString(),password.getText().toString());
                 if(map.get(RESTApiConstant.RESPONSE_CODE_KEY).equalsIgnoreCase("200"))
                 {
                     JWTToken token=new JWTToken();
                     token.setToken(map.get(RESTApiConstant.RESPONSE_VALUE_KEY));

                     Toast.makeText(getApplicationContext(),token.getToken(),Toast.LENGTH_LONG).show();
                 }
                 else if(map.get(RESTApiConstant.RESPONSE_CODE_KEY).equalsIgnoreCase("500"))
                 {
                     Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                 }

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


}