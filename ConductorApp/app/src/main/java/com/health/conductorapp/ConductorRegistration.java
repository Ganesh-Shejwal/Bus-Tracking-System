package com.health.conductorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class ConductorRegistration extends AppCompatActivity {

    EditText conductorName, conductorContact, conductorVehicleNumber, conductorUserName, conductorPassword;
    LinearLayout conductorRegister;
    ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_registration);

        conductorName = findViewById(R.id.conductor_name);
        conductorContact = findViewById(R.id.contact_number);
        conductorVehicleNumber = findViewById(R.id.vehicle_number);
        conductorUserName = findViewById(R.id.user_name);
        conductorPassword = findViewById(R.id.password);
        conductorRegister = findViewById(R.id.lin_register);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        conductorRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                if (!conductorName.getText().toString().isEmpty()&&!conductorContact.getText().toString().isEmpty()
                        &&!conductorVehicleNumber.getText().toString().isEmpty()&&!conductorUserName.getText().toString().isEmpty()&&!conductorPassword.getText().toString().isEmpty())
                {
                    if(conductorName.getText().toString().matches("^[a-zA-Z]*$")&&conductorContact.getText().toString().length()==10)
                    {
                        registerConductor();
                    }
                    else {
                        Toast.makeText(ConductorRegistration.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ConductorRegistration.this, "All Fields Are Requird...", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void registerConductor() {
        simpleProgressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.REGISTER_CONDUCTOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    boolean isTrue = object.getBoolean("isSuccessful");
                    simpleProgressBar.setVisibility(View.GONE);
                    if (isTrue == true) {
                        Toast.makeText(ConductorRegistration.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(ConductorRegistration.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.d("error", "" + e);
                    simpleProgressBar.setVisibility(View.GONE);
                    Toast.makeText(ConductorRegistration.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                simpleProgressBar.setVisibility(View.GONE);
                Toast.makeText(ConductorRegistration.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("conductorName", conductorName.getText().toString());
                hashMap.put("contactNumber", conductorContact.getText().toString());
                hashMap.put("vehicleNumber", conductorVehicleNumber.getText().toString());
                hashMap.put("userName", conductorUserName.getText().toString());
                hashMap.put("password", conductorPassword.getText().toString());
                Log.d("hashMap", hashMap.toString());
                return hashMap;

            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(ConductorRegistration.this);
        requestQueue.add(request);
    }
}