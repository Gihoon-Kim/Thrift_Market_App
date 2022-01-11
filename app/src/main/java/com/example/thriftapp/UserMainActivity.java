package com.example.thriftapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import butterknife.OnItemSelected;

public class UserMainActivity extends AppCompatActivity implements TextWatcher {

    private final static String TAG = "UserMainActivity";
    private final static int GET_ALL_PRODUCTS = 1;
    private final static int GET_MY_PRODUCTS = 2;
    private String userName;
    private int userNumber;

    private int SEARCH_OPTION = 0;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvItemList)
    RecyclerView rvItemList;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.spinnerSearch)
    Spinner spinnerSearch;

    ArrayList<ProductsInformation> list = null;
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
                                    resultIntent.getStringExtra("tradeLocation"),
                                    resultIntent.getStringExtra("addedDate"),
                                    resultIntent.getIntExtra("productOwnerNumber", 0),
                                    resultIntent.getParcelableExtra("image")
                            );
                            list.add(productsInformation);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
        );
        rvItemList.setLayoutManager(new LinearLayoutManager(this));

        CallRecyclerView();

        /*
        Create custom listener object and send
         */
        MakeAdapterClickable();

        // Searching options
        String[] searchList = getResources().getStringArray(R.array.search_list);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                searchList
        );
        spinnerSearch.setAdapter(spinnerAdapter);

        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SEARCH_OPTION = position;
                Log.i(TAG, "Search Option = " + SEARCH_OPTION);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void MakeAdapterClickable() {
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

                ContactDialog contactDialog = new ContactDialog(
                        UserMainActivity.this,
                        list.get(position).getProductNumber(),
                        list.get(position).getProductName(),
                        list.get(position).getProductDesc(),
                        list.get(position).getProductPrice(),
                        list.get(position).getTradeLocation(),
                        list.get(position).getImageBitmap(),
                        list.get(position).getProductOwnerNumber()
                );
                contactDialog.CallDialog();
            }
        });
    }

    private void CallRecyclerView() {

        list = new ArrayList<>();

        // 1 for all
        // 2 for mine
        AddProductsIntoList(list, GET_ALL_PRODUCTS);

        adapter = new ProductAdapter(list, getApplicationContext());
        rvItemList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        MakeAdapterClickable();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnAddNew)
    public void OnAddNewBtn() {

        Intent intent = new Intent(UserMainActivity.this, AddNewProduct.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userNumber", userNumber);
        resultLauncher.launch(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnViewMine)
    public void OnViewMine() {

        list = new ArrayList<>();

        // 1 for all
        // 2 for mine
        AddProductsIntoList(list, GET_MY_PRODUCTS);

        adapter = new ProductAdapter(list, getApplicationContext());
        rvItemList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        MakeAdapterClickable();

        Button btnViewMine = findViewById(R.id.btnViewMine);
        Button btnViewAll = findViewById(R.id.btnViewAll);

        btnViewMine.setVisibility(View.GONE);
        btnViewAll.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnViewAll)
    public void OnViewAll() {

        CallRecyclerView();
        Button btnViewMine = findViewById(R.id.btnViewMine);
        Button btnViewAll = findViewById(R.id.btnViewAll);

        btnViewMine.setVisibility(View.VISIBLE);
        btnViewAll.setVisibility(View.GONE);
    }

    private void AddProductsIntoList(ArrayList<ProductsInformation> list, int id) {

        Response.Listener<String> responseListener = response -> {

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("Products");

                Log.i(TAG, jsonArray.toString());

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    boolean success = item.getBoolean("success");

                    if (success) {

                        String productNumber = item.getString("ProductNumber");
                        String productName = item.getString("ProductName");
                        String productDesc = item.getString("ProductDesc");
                        int productOwnerNumber = item.getInt("ProductOwnerNumber");
                        String productOwner = item.getString("OwnerName");
                        String productPrice = String.valueOf(item.getDouble("ProductPrice"));
                        String tradeLocation = item.getString("TradeLocation");
                        String addedDate = item.getString("AddedDate");
                        String imageBase64 = item.getString("ImageFile");
                        // Get image from server, and decode to bitmap.
                        byte[] decodeBase64 = Base64.decode(imageBase64, 0);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeBase64, 0, decodeBase64.length);
                        Log.i(TAG, "Decode to Bitmap : " + bitmap.toString());

                        ProductsInformation productsInformation = new ProductsInformation(
                                productNumber,
                                productName,
                                productDesc,
                                productOwner,
                                productPrice,
                                tradeLocation,
                                addedDate,
                                productOwnerNumber,
                                bitmap
                        );
                        list.add(productsInformation);
                        adapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        };

        if (id == GET_ALL_PRODUCTS) {
            GetProductRequest getProductRequest = new GetProductRequest(responseListener);
            RequestQueue queue = Volley.newRequestQueue(UserMainActivity.this);
            queue.add(getProductRequest);
        }  else if (id == GET_MY_PRODUCTS) {

            GetMyProductRequest getMyProductRequest = new GetMyProductRequest(userNumber, responseListener);
            RequestQueue queue = Volley.newRequestQueue(UserMainActivity.this);
            queue.add(getMyProductRequest);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        adapter.setSearchOption(SEARCH_OPTION);
        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}