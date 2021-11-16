package com.example.thriftapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetProductRequest extends StringRequest {

    private final static String URL = "http://10.0.2.2/TMA_GetPRoducts.php";
    private Map<String, String> map;

    public GetProductRequest(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
