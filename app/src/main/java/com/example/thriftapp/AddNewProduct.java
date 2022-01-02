package com.example.thriftapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewProduct extends AppCompatActivity {

    private static final String TAG = "AddNewProduct";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etProductName)
    EditText etProductName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etProductDesc)
    EditText etProductDesc;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etProductPrice)
    EditText etProductPrice;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etLocation)
    EditText etTradeLocation;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvImages)
    RecyclerView rvImages;
    @BindView(R.id.btnAddImage)
    Button btnAddImage;

    private int ownerNumber;
    private String ownerName;
    ImageBottomSheetDialog bottomSheetDialog;

    MultiImageAdapter multiImageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_product);

        Intent intent = getIntent();
        ownerNumber = intent.getIntExtra("userNumber", 0);
        ownerName = intent.getStringExtra("userName");

        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnAddImage)
    public void OnAddImage() {

        bottomSheetDialog = new ImageBottomSheetDialog(getApplicationContext(), ivPhoto, rvImages, btnAddImage);
        bottomSheetDialog.show(getSupportFragmentManager(), "BottomSheet");
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnSave)
    public void OnSaveProduct() {

        String productName = etProductName.getText().toString().trim();
        String productDesc = etProductDesc.getText().toString().trim();
        float productPrice;
        String tradeLocation = etTradeLocation.getText().toString().trim();

        // Current date
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(date);

        if (etProductPrice.getText().toString().trim().equals("")) {

            productPrice = 0;
        } else {

            productPrice = Float.parseFloat(etProductPrice.getText().toString().trim());
        }

        if (productName.equals("") || productPrice < 0 || btnAddImage.getVisibility() != View.GONE) {

            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (productName.equals("")) {

                dialog = builder
                        .setMessage("Error cause : \n - Product Name should not be empty")
                        .setPositiveButton("OK", null)
                        .create();
                dialog.show();
            } else if (productPrice < 0) {

                dialog = builder
                        .setMessage("Error cause : \n - Product price must higher than 0")
                        .setPositiveButton("OK", null)
                        .create();
                dialog.show();
            } else {

                dialog = builder
                        .setMessage("Error cause : \n - Image must be added")
                        .setPositiveButton("OK", null)
                        .create();
                dialog.show();
            }
            return;
        }

        Response.Listener<String> addProductListener = response -> {

            try {

                Log.i(TAG, "Add Product Listener Response = " + response);
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
                    intent.putExtra("location", tradeLocation);
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

        Response.Listener<String> addProductImageListener = response -> {

            try {

                Log.i(TAG, "Add Product Image Listener Response = " + response);
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                Intent intent = new Intent();

                if (success) {

                    Toast.makeText(AddNewProduct.this, "Product Image is added", Toast.LENGTH_LONG).show();

                    intent.putExtra("callType", 1);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {

                    Toast.makeText(AddNewProduct.this, "Product Image is NOT added", Toast.LENGTH_LONG).show();
                    intent.putExtra("callType", 0);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        };

        // Get image and encode to bitmap, add to ArrayList
        ArrayList<String> imgArrayList = new ArrayList<>();
        int numOfImages;
        if (ivPhoto.getVisibility() == View.VISIBLE) {

            BitmapDrawable bitmapDrawable = (BitmapDrawable) ivPhoto.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            imgArrayList.add(IncodetoBitmap(bitmap));
            numOfImages = 1;
        } else {

            multiImageAdapter = bottomSheetDialog.getAdapter();
            Log.i(TAG, String.valueOf(multiImageAdapter.getItemCount()));

            for (int i = 0; i < multiImageAdapter.getItemCount(); i++) {

                Log.i(TAG, "recycler view uri : " + multiImageAdapter.getUri(i).toString() + "\n");

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            multiImageAdapter.getUri(i)
                    );
                    imgArrayList.add(IncodetoBitmap(bitmap));
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            numOfImages = multiImageAdapter.getItemCount();
        }

        // Convert ArrayList to JSON
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < imgArrayList.size(); i++) {

            try {

                jsonObject.put("Count" + (i + 1), imgArrayList.get(i));
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        // Add Product into database
        AddProductRequest addProductRequest = new AddProductRequest(
                productName,
                productDesc,
                productPrice,
                ownerNumber,
                tradeLocation,
                formattedDate,
                addProductListener
        );
        RequestQueue addProductQueue = Volley.newRequestQueue(this);
        addProductQueue.add(addProductRequest);

        // Add Product Images into database
        AddProductImageRequest addProductImagesRequest = new AddProductImageRequest(
                productName,
                numOfImages,
                jsonObject,
                ownerNumber,
                addProductImageListener
        );
        RequestQueue addProductImagesQueue = Volley.newRequestQueue(this);
        addProductImagesQueue.add(addProductImagesRequest);
    }

    private String IncodetoBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap = resize(bitmap);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap resize(Bitmap bm) {
        Configuration config=getResources().getConfiguration();
        if(config.smallestScreenWidthDp>=800)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if(config.smallestScreenWidthDp>=600)
            bm = Bitmap.createScaledBitmap(bm, 150, 90, true);
        else if(config.smallestScreenWidthDp>=400)
            bm = Bitmap.createScaledBitmap(bm, 100, 60, true);
        else if(config.smallestScreenWidthDp>=360)
            bm = Bitmap.createScaledBitmap(bm, 90, 54, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 80, 48, true);
        return bm;
    }
}
