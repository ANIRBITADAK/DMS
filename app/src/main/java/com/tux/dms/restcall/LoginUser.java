package com.tux.dms.restcall;


import com.tux.dms.constantkeys.RESTApiConstant;
import com.tux.dms.dto.JWTToken;
import com.tux.dms.dto.UserCredential;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUser extends RESTBaseClass {

    public Map<String,String> handleLogin(String email, String Password) {

        Map<String,String> map=new HashMap<>();
        UserCredential user=new UserCredential();
        user.setPassword(Password);
        user.setEmail(email);
        Call<JWTToken> call = retrofitInterface.executeLogin(user);


        call.enqueue( new Callback<JWTToken>() {
            @Override
            public void onResponse(Call<JWTToken> call, Response<JWTToken> response) {

                JWTToken token = response.body();
                map.put(RESTApiConstant.RESPONSE_CODE_KEY,Integer.toString(response.code()));
                map.put(RESTApiConstant.RESPONSE_VALUE_KEY,token.getToken());
            }

            @Override
            public void onFailure(Call<JWTToken> call, Throwable t) {

                map.put(RESTApiConstant.RESPONSE_CODE_KEY,Integer.toString(500));
                map.put(RESTApiConstant.RESPONSE_VALUE_KEY,t.getMessage());
            }
        });

        return map;

    }

}
