package com.example.thriftapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductRequest extends StringRequest {

    private final static String URL = "http://hoonyhosting.dothome.co.kr/php/TMA_AddProduct.php";
    private final Map<String, String> map;
    public AddProductRequest(String productName, String productDesc, float productPrice, int ownerNumber, String location, String formattedDate, JSONObject image, int numOfImages, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("productName", productName);
        map.put("productDesc", productDesc);
        map.put("productPrice", String.valueOf(productPrice));
        map.put("productOwner", String.valueOf(ownerNumber));
        map.put("location", location);
        map.put("AddedDate", formattedDate);
        map.put("images", image.toString());
        map.put("numOfImages", String.valueOf(numOfImages));
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
