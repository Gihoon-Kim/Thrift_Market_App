package com.example.thriftapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvErrorMsg)
    TextView tvErrorMsg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ButterKnife.bind(this);
    }

    // Registration Function -- Open Registration Activity.
    @OnClick(R.id.tvCreateAccount)
    public void IntentToRegisterActivity() {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Login
    @OnClick(R.id.btnLogin)
    public void OnLogin() {

        String mLogin_userID = etEmail.getText().toString().trim();
        String mLogin_userPwd = etPassword.getText().toString().trim();

        if (mLogin_userID.equals("") || mLogin_userPwd.equals("")) {

            tvErrorMsg.setText("Login Fail. Check your ID and Password");
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

                    tvErrorMsg.setText("Login Fail. Check your ID and Password");
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