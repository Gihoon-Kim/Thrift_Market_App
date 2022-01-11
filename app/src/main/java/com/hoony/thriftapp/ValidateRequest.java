package com.hoony.thriftapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {

    private final static String URL = "http://hoonyhosting.dothome.co.kr/php/TMA_UserValidate.php";
    private final Map<String, String> param;

    public ValidateRequest(String userEmail, Response.Listener<String> listener) {

        super(Method.POST, URL, listener, null);
        param = new HashMap<>();
        param.put("userEmail", userEmail);
    }

    public Map<String, String> getParams() {

        return param;
    }
}
