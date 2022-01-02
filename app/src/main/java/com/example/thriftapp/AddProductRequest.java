package com.example.thriftapp;

import android.util.Log;

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
    private final static String TAG = "AddProductRequest";
    public AddProductRequest(String productName, String productDesc, float productPrice, int ownerNumber, String location, String formattedDate, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        Log.i(TAG, "Add Product Request executed");
        map = new HashMap<>();
        map.put("productName", productName);
        map.put("productDesc", productDesc);
        map.put("productPrice", String.valueOf(productPrice));
        map.put("productOwner", String.valueOf(ownerNumber));
        map.put("location", location);
        map.put("AddedDate", formattedDate);
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
