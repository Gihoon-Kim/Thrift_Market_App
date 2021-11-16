package com.example.thriftapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewProduct extends AppCompatActivity {

    private static final String TAG = "AddNewProduct";

    @BindView(R.id.etProductName)
    EditText etProductName;
    @BindView(R.id.etProductDesc)
    EditText etProductDesc;
    @BindView(R.id.etProductPrice)
    EditText etProductPrice;

    private int ownerNumber;
    private String ownerName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_product);

        Intent intent = getIntent();
        ownerNumber = intent.getIntExtra("userNumber", 0);
        ownerName = intent.getStringExtra("userName");

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSave)
    public void OnSaveProduct() {

        String productName = etProductName.getText().toString().trim();
        String productDesc = etProductDesc.getText().toString().trim();
        float productPrice;
        if (etProductPrice.getText().toString().trim().equals("")) {

            productPrice = 0;
        } else {

            productPrice = Float.parseFloat(etProductPrice.getText().toString().trim());
        }

        if (productName.equals("") || productPrice < 0) {

            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder
                    .setMessage("Error cause : \n - Product Name should not be empty \n - Product Price should be higher than 0")
                    .setPositiveButton("OK", null)
                    .create();
            dialog.show();
            return;
        }

        Response.Listener<String> responseListener = response -> {

            try {

                Log.i(TAG, response);
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                Intent intent = new Intent();

                if (success) {

                    Toast.makeText(AddNewProduct.this, "Product is added", Toast.LENGTH_LONG).show();

                    intent.putExtra("callType", 1);
                    intent.putExtra("productName", productName);
                    intent.putExtra("productDesc", productDesc);
                    intent.putExtra("productPrice", String.valueOf(productPrice));
                    intent.putExtra("productOwner", ownerName);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {

                    Toast.makeText(AddNewProduct.this, "Product is NOT added", Toast.LENGTH_LONG).show();
                    intent.putExtra("callType", 0);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        };

        AddProductRequest addProductRequest = new AddProductRequest(
                productName,
                productDesc,
                productPrice,
                ownerNumber,
                responseListener
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(addProductRequest);
    }
}
