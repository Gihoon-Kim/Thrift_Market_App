package com.example.thriftapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @OnClick(R.id.ivPhoto)
    public void OnAddImage() {

        bottomSheetDialog = new ImageBottomSheetDialog(getApplicationContext(), ivPhoto, rvImages);
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


        ArrayList<String> imgArrayList = new ArrayList<>();
        int numOfImages = 0;
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

        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < imgArrayList.size(); i++) {

            try {

                jsonObject.put("Count" + (i + 1), imgArrayList.get(i));
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        AddProductRequest addProductRequest = new AddProductRequest(
                productName,
                productDesc,
                productPrice,
                ownerNumber,
                tradeLocation,
                formattedDate,
                jsonObject,
                numOfImages,
                responseListener
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(addProductRequest);
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
