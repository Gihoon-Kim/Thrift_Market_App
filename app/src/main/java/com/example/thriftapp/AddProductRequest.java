package com.example.thriftapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddProductRequest extends StringRequest {

    private final static String URL = "http://10.0.2.2/TMA_AddProduct.php";
    private Map<String, String> map;
    public AddProductRequest(String productName, String productDesc, float productPrice, int ownerNumber, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("productName", productName);
        map.put("productDesc", productDesc);
        map.put("productPrice", String.valueOf(productPrice));
        map.put("productOwner", String.valueOf(ownerNumber));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}