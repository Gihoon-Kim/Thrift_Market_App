package com.hoony.thriftapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdateDeleteProductDialog {

    private final static String TAG = "UpdateDeleteProductDlg";

    EditText etProductName;
    EditText etProductDesc;
    EditText etProductPrice;
    Spinner spinnerStatus;

    private Context context;
    private String productNumber;
    private String productName;
    private String productDesc;
    private String productPrice;
    private Dialog dialog;
    private ProductAdapter adapter;
    private int position;
    private ArrayList<ProductsInformation> list;

    public UpdateDeleteProductDialog(
            Context context,
            String productNumber,
            String productName,
            String productDesc,
            String productPrice,
            ProductAdapter adapter,
            int position,
            ArrayList<ProductsInformation> list
    ) {

        this.context = context;
        this.productNumber = productNumber;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.adapter = adapter;
        this.position = position;
        this.list = list;
    }

    public void CallDialog() {

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_ownproduct);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

        /*
        Dialog Click Event Listeners
         */
        etProductName = dialog.findViewById(R.id.etProductName);
        etProductDesc = dialog.findViewById(R.id.etProductDesc);
        etProductPrice = dialog.findViewById(R.id.etProductPrice);
        final Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        final Button btnDelete = dialog.findViewById(R.id.btnDelete);
        spinnerStatus = dialog.findViewById(R.id.spinnerStatus);

        etProductName.setText(productName);
        etProductDesc.setText(productDesc);
        etProductPrice.setText(productPrice);
        btnUpdate.setOnClickListener(updateListener);
        btnDelete.setOnClickListener(deleteListener);

        String[] status = context.getResources().getStringArray(R.array.status);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                status
        );
        spinnerStatus.setAdapter(spinnerAdapter);
    }

    private final View.OnClickListener updateListener = v -> {

        String newName = etProductName.getText().toString();
        String newDesc = etProductDesc.getText().toString();
        float newPrice;
        String status = spinnerStatus.getSelectedItem().toString();

        try {

            if (newName.equals("") || newDesc.equals("")) {

                Toast.makeText(context, "Product Name and Description cannot be empty", Toast.LENGTH_SHORT).show();
            } else {

                newPrice = Float.parseFloat(etProductPrice.getText().toString().trim());

                Response.Listener<String> responseListener = response -> {

                    try {

                        Log.i(TAG, "new Name = " + newName + ", new Desc = " + newDesc);
                        Log.i(TAG, response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {

                            Log.i(TAG, response);
                        } else {

                            Toast.makeText(context, "Update Fail", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                };

                UpdateProductRequest updateProductRequest = new UpdateProductRequest(
                        productNumber,
                        newName,
                        newDesc,
                        newPrice,
                        status,
                        responseListener
                );
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(updateProductRequest);

                list.get(position).setProductName(newName);
                productName = newName;
                list.get(position).setProductDesc(newDesc);
                productDesc = newDesc;
                list.get(position).setProductPrice(String.valueOf(newPrice));
                productPrice = String.valueOf(newPrice);
                list.get(position).setStatus(status);
            }

        } catch (NumberFormatException e) {

            e.printStackTrace();
            Toast.makeText(context, "Price should be a decimal number", Toast.LENGTH_SHORT).show();
        }

        dialog.dismiss();
        adapter.notifyDataSetChanged();
    };

    private final View.OnClickListener deleteListener = v -> {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog tempDialog = builder
                .setMessage("Do you really want to Delete the product?")
                .setPositiveButton("Yes", (dialog1, which) ->  {

                    Response.Listener<String> responseListener = response -> {

                        try {

                            Log.i(TAG, "Delete processing");
                            Log.i(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {

                                Log.i(TAG, "Delete Success");
                                Toast.makeText(context, "Item Deleted successfully", Toast.LENGTH_SHORT).show();
                                list.remove(list.get(position));
                                adapter.notifyDataSetChanged();

                                dialog.dismiss();
                            } else {

                                Toast.makeText(context, "Deleting failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    };

                    DeleteProductRequest deleteProductRequest = new DeleteProductRequest(
                            productNumber,
                            productName,
                            responseListener
                    );
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(deleteProductRequest);
                })
                .setNegativeButton("No", null)
                .create();
        tempDialog.show();
    };
}
