package com.example.thriftapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private final static String URL = "http://hoonyhosting.dothome.co.kr/php/TMA_UserRegister.php";
    private final Map<String, String> map;

    public RegisterRequest(String userEmail, String userName, String userPwd, String userPhoneNumber, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userName", userName);
        map.put("userEmail", userEmail);
        map.put("userPwd", userPwd);
        map.put("userPhoneNumber", userPhoneNumber);
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
