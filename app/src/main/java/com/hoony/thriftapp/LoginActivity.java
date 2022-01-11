package com.hoony.thriftapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*

    Login Function - Matches ID and Password with Database.
    Registration Function - Open Registration Activity.

 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etEmail)
    EditText etEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etPassword)
    EditText etPassword;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvErrorMsg)
    TextView tvErrorMsg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ButterKnife.bind(this);

        // Authorization request
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "Authorization setting complete");
            } else {

                Log.d(TAG, "Authorization setting Request");
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        1
                );
            }
        }
    }

    // Request Authorization
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult");

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    // Registration Function -- Open Registration Activity.
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvCreateAccount)
    public void IntentToRegisterActivity() {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Login
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnLogin)
    public void OnLogin() {

        String mLogin_userID = etEmail.getText().toString().trim();
        String mLogin_userPwd = etPassword.getText().toString().trim();

        if (mLogin_userID.equals("") || mLogin_userPwd.equals("")) {

            tvErrorMsg.setText(R.string.login_fail);
            etEmail.setText("");
            etPassword.setText("");
            return;
        }

        Response.Listener<String> responseListener = response -> {

            try {

                Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                Log.i(TAG, response);
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");

                if (success) {

                    // Pass ID(Email), Password, Name, Phone Number
                    Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                    String userName = jsonObject.getString("UserName");
                    int userNumber = jsonObject.getInt("UserNumber");

                    intent.putExtra("userName", userName);
                    intent.putExtra("userNumber", userNumber);
                    startActivity(intent);
                } else {

                    tvErrorMsg.setText(R.string.login_fail);
                    etEmail.setText(null);
                    etPassword.setText(null);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        };

        LoginRequest loginRequest = new LoginRequest(mLogin_userID, mLogin_userPwd, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }
}