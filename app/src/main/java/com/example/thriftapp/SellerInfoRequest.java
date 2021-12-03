package com.example.thriftapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SellerInfoRequest extends StringRequest {

    private final static String URL = "http://10.0.2.2/TMA_GetSellerInfo.php";
    private final Map<String, String> map;

    public SellerInfoRequest(String productNumber, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("productNumber", productNumber);
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
