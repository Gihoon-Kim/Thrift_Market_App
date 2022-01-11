package com.hoony.thriftapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetImagesRequest extends StringRequest {

    private final static String URL = "http://hoonyhosting.dothome.co.kr/php/TMA_GetImages.php";
    private final Map<String, String> map;

    public GetImagesRequest(String productName, int productOwner, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("productName", productName);
        map.put("productOwner", String.valueOf(productOwner));
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
