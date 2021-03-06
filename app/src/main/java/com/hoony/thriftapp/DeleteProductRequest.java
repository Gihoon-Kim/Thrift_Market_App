package com.hoony.thriftapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteProductRequest extends StringRequest {

    private final static String URL = "http://hoonyhosting.dothome.co.kr/php/TMA_DeleteProduct.php";
    private final Map<String, String> map;

    public DeleteProductRequest(String productNumber, String productName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("productNumber", productNumber);
        map.put("productName", productName);
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
