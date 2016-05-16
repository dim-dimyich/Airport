package com.qoobico.remindme.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.qoobico.remindme.R;
import com.qoobico.remindme.app.EndPoints;
import com.qoobico.remindme.app.MyApplication;
import com.qoobico.remindme.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Winner on 01.12.2015.
 */
public class LogInActivity extends AppCompatActivity {
    private String TAG = LogInActivity.class.getSimpleName();
    private EditText inputPassword, inputEmail;
    private TextInputLayout inputLayoutPassword, inputLayoutEmail;
    private Button btnEnter;
    private ArrayList<User> UsersArrayList;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request


        // changing action bar colo
        /**
         * Check for login session. It user is already logged in
         * redirect him to main activity
         * */
        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.log_in);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputEmail = (EditText) findViewById(R.id.input_email);
        btnEnter = (Button) findViewById(R.id.btn_enter);


        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */
    private void login() {
        if (!validatePassword()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }


        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // user successfully logged in
                        JSONArray UsersArray = obj.getJSONArray("user");

                        pDialog.setMessage("Загрузка...");
                        pDialog.show();

                            JSONObject UsersObj = (JSONObject) UsersArray.get(0);
                            User us = new User();
                            us.setId(UsersObj.getString("user_id"));
                            us.setName(UsersObj.getString("name"));
                            us.setPhone(UsersObj.getString("phone"));
                            us.setEmail(UsersObj.getString("email"));
                            us.setImageUser(UsersObj.getString("user_image"));
                            us.setPosition(UsersObj.getString("position_name"));
                            us.setCost(UsersObj.getString("cost_per_hour"));

                            // storing user in shared preferences
                            MyApplication.getInstance().getPrefManager().storeUser(us);

                        // start main activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getApplicationContext(), "Else" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Catch json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Неверный логин или пароль ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Нет доступа к сети интернет " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Нет доступа к сети интернет ", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    // Validating name
    private boolean validatePassword() {

        return true;
    }

    // Validating email
    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }


        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

}