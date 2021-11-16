package com.example.thriftapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {

    private final static String URL = "http://10.0.2.2/TMA_UserValidate.php";
    private Map<String, String> param;

    public ValidateRequest(String userEmail, Response.Listener<String> listener) {

        super(Method.POST, URL, listener, null);
        param = new HashMap<>();
        param.put("userEmail", userEmail);
    }

    public Map<String, String> getParams() {

        return param;
    }
}
