package com.example.thriftapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserMainActivity extends AppCompatActivity implements TextWatcher {

    private final static String TAG = "UserMainActivity";
    private String userName;
    private int userNumber;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvItemList)
    RecyclerView rvItemList;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etSearch)
    EditText etSearch;

    ArrayList<ProductsInformation> list;
    ProductAdapter adapter;
    
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_activity);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userNumber = intent.getIntExtra("userNumber", 0);

        ButterKnife.bind(this);
        etSearch.addTextChangedListener(this);

        // Activity Callback Method
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK) {

                        Intent resultIntent = result.getData();
                        assert resultIntent != null;
                        int callType = resultIntent.getIntExtra("callType", 0);

                        if (callType == 1) {

                            ProductsInformation productsInformation = new ProductsInformation(
                                    resultIntent.getStringExtra("productNumber"),
                                    resultIntent.getStringExtra("productName"),
                                    resultIntent.getStringExtra("productDesc"),
                                    resultIntent.getStringExtra("productOwner"),
                                    resultIntent.getStringExtra("productPrice"),
                                    resultIntent.getStringExtra("tradeLocation")
                            );
                            list.add(productsInformation);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        CallRecyclerView();

        /*
        Create custom listener object and send
         */
        adapter.SetOnItemClickListener((v, position) -> {

            // Item Click Event
            Log.i(TAG, "Item Clicked");
            if (userName.equals(list.get(position).getProductOwner())) {

                Log.i(TAG, list.get(position).getProductNumber() + " " + list.get(position).getProductName() + " " + list.get(position).getProductDesc() + " " + list.get(position).getProductPrice());
                UpdateDeleteProductDialog mDialog = new UpdateDeleteProductDialog(
                        UserMainActivity.this,
                        list.get(position).getProductNumber(),
                        list.get(position).getProductName(),
                        list.get(position).getProductDesc(),
                        list.get(position).getProductPrice(),
                        adapter,
                        position,
                        list
                );
                mDialog.CallDialog();
            } else {

                ContactDialog connectDialog = new ContactDialog(
                        UserMainActivity.this,
                        list.get(position).getProductNumber(),
                        list.get(position).getProductName(),
                        list.get(position).getProductDesc(),
                        list.get(position).getProductPrice(),
                        list.get(position).getTradeLocation()
                );
                connectDialog.CallDialog();
            }
        });
    }

    private void CallRecyclerView() {

        list = new ArrayList<>();

        Response.Listener<String> responseListener = response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("Products");

                Log.i(TAG, jsonArray.toString());
                Log.i(TAG, String.valueOf(jsonArray.length()));
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    boolean success = item.getBoolean("success");
                    if (success) {

                        String productNumber = item.getString("ProductNumber");
                        String productName = item.getString("ProductName");
                        String productDesc = item.getString("ProductDesc");
                        String productOwner = item.getString("ProductOwner");
                        String productPrice = String.valueOf(item.getDouble("ProductPrice"));
                        String tradeLocation = item.getString("TradeLocation");

                        ProductsInformation productsInformation = new ProductsInformation(productNumber, productName, productDesc, productOwner, productPrice, tradeLocation);
                        list.add(productsInformation);
                    }

                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        };

        GetProductRequest getProductRequest = new GetProductRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(UserMainActivity.this);
        queue.add(getProductRequest);

        rvItemList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductAdapter(list);
        rvItemList.setAdapter(adapter);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnAddNew)
    public void OnAddNewBtn() {

        Intent intent = new Intent(UserMainActivity.this, AddNewProduct.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userNumber", userNumber);
        resultLauncher.launch(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}