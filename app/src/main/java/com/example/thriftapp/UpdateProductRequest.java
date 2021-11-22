package com.example.thriftapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateProductRequest extends StringRequest {

    private final static String URL = "http://10.0.2.2/TMA_UpdateProduct.php";
    private final Map<String, String> map;

    public UpdateProductRequest(String productNumber, String productName, String productDesc, float productPrice, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("productNumber", productNumber);
        map.put("productName", productName);
        map.put("productDesc", productDesc);
        map.put("productPrice", String.valueOf(productPrice));
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
