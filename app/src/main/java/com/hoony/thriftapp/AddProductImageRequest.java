package com.hoony.thriftapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddProductImageRequest extends StringRequest {

    private final static String URL = "http://hoonyhosting.dothome.co.kr/php/TMA_AddProductImage.php";
    private final Map<String, String> map;

    public AddProductImageRequest(String productName, int numOfImages, JSONObject image, int ownerNumber, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("productName", productName);
        map.put("numOfImages", String.valueOf(numOfImages));
        map.put("images", image.toString());
        map.put("productOwner", String.valueOf(ownerNumber));
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
