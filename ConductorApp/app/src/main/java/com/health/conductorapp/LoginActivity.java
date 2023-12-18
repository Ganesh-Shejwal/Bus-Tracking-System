package com.health.conductorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText et_username, et_password;
    Button btn_login;

    String conductorName, contactNumber, vehicleNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Login();
    }


    void Login() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(et_username)) {
                    et_username.setError("user Name Required");


                } else if (isEmpty(et_password)) {
                    et_password.setError("password Required");
                } else {

                    if (et_username.getText().toString().equals("admin") && et_password.getText().toString().equals("admin@123")) {
                        Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
                        startActivity(intent);
                        finish();

                    } else {
                        userLogin(et_username.getText().toString(), et_password.getText().toString());
                    }

                }

            }
        });
    }


    public void userLogin(String userName, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, Urls.CONDUCTOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response", response);

                try {
                    JSONObject object = new JSONObject(response);

                    String code = object.getString("code");
                    String status = object.getString("ResponseMessage");

                    if (code.equals("1")) {

                        JSONObject jsonObject = object.getJSONObject("userDetails");

                        conductorName = jsonObject.getString("name");
                        contactNumber = jsonObject.getString("contactNumber");
                        vehicleNumber = jsonObject.getString("vehicleNumber");


                        Toast.makeText(LoginActivity.this, "" + status, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("conductorName", conductorName);
                        intent.putExtra("mobileNumber", contactNumber);
                        intent.putExtra("vehicleNumber", vehicleNumber);
                        startActivity(intent);
                        finish();


                    } else {
                        Toast.makeText(LoginActivity.this, "" + status, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    Log.d("error", "" + e);
                    Toast.makeText(getApplicationContext(), "Please try after some time", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                Toast.makeText(getApplicationContext(), "Please try after some time", Toast.LENGTH_SHORT).show();


            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userName", userName);
                hashMap.put("password", password);
                Log.d("hashMap", hashMap.toString());
                return hashMap;

            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(request);

    }


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

}