package com.example.thriftapp;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    private final static String URL = "http://10.0.2.2/TMA_UserLogin.php";
    private Map<String, String> map;

    public LoginRequest(String userEmail, String userPwd, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userEmail", userEmail);
        map.put("userPassword", userPwd);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
