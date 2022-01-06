package com.example.thriftapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactDialog {

    private final static String TAG = "ConnectDialog";

    TextView tvSellerName;
    TextView tvSellerEmail;
    TextView tvSellerPhone;
    TextView tvSellerTradeCount;
    TextView tvProductName;
    TextView tvProductDesc;
    TextView tvProductPrice;
    TextView tvTradeLocation;
    ImageView ivImage;

    private final Context context;
    private final String productNumber;
    private final String productName;
    private final String productDesc;
    private final String productPrice;
    private final String tradeLocation;
    private final Bitmap productImage;
    private Dialog dialog;
    private Dialog contactDialog;

    private String sellerName;
    private String sellerEmail;
    private String sellerTradeCount;
    private String sellerPhone;

    public ContactDialog(
            Context context,
            String productNumber,
            String productName,
            String productDesc,
            String productPrice,
            String tradeLocation,
            Bitmap productImage
    ) {

        this.context = context;
        this.productNumber = productNumber;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.tradeLocation = tradeLocation;
        this.productImage = productImage;
    }

    public void CallDialog() {

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_othersproduct);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

        tvSellerName = dialog.findViewById(R.id.tvSellerName);
        tvSellerEmail = dialog.findViewById(R.id.tvSellerEmail);
        tvSellerPhone = dialog.findViewById(R.id.tvSellerPhone);
        tvSellerTradeCount = dialog.findViewById(R.id.tvSellerTradeCount);

        tvProductName = dialog.findViewById(R.id.tvProductName);
        tvProductDesc = dialog.findViewById(R.id.tvProductDesc);
        tvProductPrice = dialog.findViewById(R.id.tvProductPrice);
        tvTradeLocation = dialog.findViewById(R.id.tvTradeLocation);
        ivImage = dialog.findViewById(R.id.ivPhoto);

        Button btnContact = dialog.findViewById(R.id.btnContact);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        tvProductName.setText(productName);
        tvProductDesc.setText(productDesc);
        tvProductPrice.setText(productPrice);
        tvTradeLocation.setText(tradeLocation);
        ivImage.setImageBitmap(Bitmap.createScaledBitmap(productImage, 300, 250, true));

        // Get Seller's information
        Response.Listener<String> responseListener = response -> {

            try {

                Log.i(TAG, response);
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");

                if (success) {

                    sellerName = jsonObject.getString("UserName");
                    sellerPhone = jsonObject.getString("UserPhoneNumber");
                    sellerEmail = jsonObject.getString("UserEmail");
                    sellerTradeCount = jsonObject.getString("TradeCount");

                    tvSellerName.setText(sellerName);
                    tvSellerEmail.setText(sellerEmail);
                    tvSellerPhone.setText(sellerPhone);
                    tvSellerTradeCount.setText(sellerTradeCount);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        };

        SellerInfoRequest sellerInfoRequest = new SellerInfoRequest(
                productNumber,
                responseListener
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(sellerInfoRequest);

        btnContact.setOnClickListener(v -> {

            // Show dialog that user can select method of connection to seller.
            // Ways should be on Phone call, Text Message, or Email
            // Create new Dialog and show
            contactDialog = new Dialog(context);
            contactDialog.setContentView(R.layout.dialog_contact_ways);
            showContactDialog();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    public void showContactDialog() {

        contactDialog.show();

        Button btnCall = contactDialog.findViewById(R.id.btnCall);
        Button btnMessage = contactDialog.findViewById(R.id.btnMessage);

        btnCall.setOnClickListener(v -> {

            Log.i(TAG, "Seller Phone : " + sellerPhone);
            context.startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + sellerPhone)));
        });

        btnMessage.setOnClickListener(v -> {

            Log.i(TAG, "Seller Phone : " + sellerPhone);
            context.startActivity(new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+sellerPhone)));
        });
    }
}