package com.hoony.thriftapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
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

/*

    Registration Activity.
    Check whether UserEmail is duplicated or not, and Password is matched to PasswordCheck.
    If no error, create new user and add in database.
 */

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "Register Activity";

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPasswordCheck)
    EditText etPasswordCheck;
    @BindView(R.id.tvErrorMsg)
    TextView tvErrorMsg;

    private boolean isValidate = false;
    private AlertDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        ButterKnife.bind(this);
    }

    // Check UserEmail is duplicated or not
    @OnClick(R.id.btnEmailValidate)
    public void OnValidate() {

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder
                    .setMessage("Please Input Email Format")
                    .setPositiveButton("OK", null)
                    .create();
            dialog.show();
            return;
        }

        String userEmail = etEmail.getText().toString();
        Toast.makeText(RegisterActivity.this, userEmail, Toast.LENGTH_LONG).show();
        Log.i(TAG, userEmail);
        if (isValidate) {
            return;  // Complete validation
        }

        if (userEmail.trim().equals("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder
                    .setMessage("Please Input User Email")
                    .setPositiveButton("OK", null)
                    .create();
            dialog.show();
            return;
        }

        Response.Listener<String> responseListener = response -> {

            try {

                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                Log.i(TAG, response);
                JSONObject jsonResponse  = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                if (success) {

                    dialog = builder
                            .setMessage("The User Email is available.")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    etEmail.setEnabled(false);  // Email Fix
                    isValidate = true;  // Validation complete
                } else {

                    dialog = builder
                            .setMessage("The User Email is already exist.")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        };

        ValidateRequest validateRequest = new ValidateRequest(userEmail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(validateRequest);
    }

    // Processing Registration
    @OnClick(R.id.btnRegister)
    public void OnRegister() {

        String userEmail = etEmail.getText().toString();
        String userName = etUserName.getText().toString();
        String userPwd = etPassword.getText().toString();
        String userPhoneNumber = etPhoneNumber.getText().toString();

        // is validate done
        if (!isValidate) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder
                    .setMessage("Validation is not completed")
                    .setPositiveButton("Ok", null)
                    .create();
            dialog.show();
            return;
        }

        if (!userPwd.equals(etPasswordCheck.getText().toString())) {

            tvErrorMsg.setText("Password and Password Checker is not matched");
        }

        if (userEmail.trim().equals("") || userName.trim().equals("") || userPwd.trim().equals("") || userPhoneNumber.trim().equals("") || etPasswordCheck.getText().toString().trim().equals("")) {

            tvErrorMsg.setText("Please fill all fields");
        }

        Response.Listener<String> responseListener = response -> {

            try {

                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");

                // Registration Success
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (success) {

                    dialog = builder
                            .setMessage("Registration is successfully done")
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    finish();
                                }
                            })
                            .create();
                    dialog.show();
                } else {

                    dialog = builder
                            .setMessage("Registration is failed")
                            .setNegativeButton("Ok", null)
                            .create();
                    dialog.show();
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        };

        // Volley request registration
        RegisterRequest registerRequest = new RegisterRequest(
                userEmail,
                userName,
                userPwd,
                userPhoneNumber,
                responseListener
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(registerRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (dialog != null) {

            dialog.dismiss();
            dialog = null;
        }
    }
}
