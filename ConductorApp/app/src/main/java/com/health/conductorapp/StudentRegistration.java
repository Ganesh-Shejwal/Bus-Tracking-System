package com.health.conductorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.Random;

public class StudentRegistration extends AppCompatActivity {

    EditText studentName, motherName, address, studentContactNumber, parentsContactNumber, email,fatherName;
    String rollNumber;
    RelativeLayout registerRelative;
    ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        // setSupportActionBar(toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        setSupportActionBar(toolbar);

        final int min = 20;
        final int max = 80;
        rollNumber = String.valueOf(new Random().nextInt((max - min) + 1) + min);

        studentName = findViewById(R.id.student_name);
        motherName = findViewById(R.id.mother_name);
        address = findViewById(R.id.address);
        studentContactNumber = findViewById(R.id.student_mobile);
        parentsContactNumber = findViewById(R.id.parents_mobile);
        email = findViewById(R.id.email);
        registerRelative = findViewById(R.id.register_relative);
        fatherName = findViewById(R.id.father_name);

        registerRelative.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                //registerStudent();
                if (!studentName.getText().toString().isEmpty()&&!motherName.getText().toString().isEmpty()&&!address.getText().toString().isEmpty()
                        &&!studentContactNumber.getText().toString().isEmpty()&&!parentsContactNumber.getText().toString().isEmpty()
                        &&!email.getText().toString().isEmpty()&&!fatherName.getText().toString().isEmpty())
                {
                    if(studentName.getText().toString().matches("^[a-zA-Z]*$")&&motherName.getText().toString().matches("^[a-zA-Z]*$")&&
                        fatherName.getText().toString().matches("^[a-zA-Z]*$")&&studentContactNumber.getText().toString().length()==10 &&
                            parentsContactNumber.getText().toString().length()==10
                    )
                    {
                        Intent intent = new Intent(StudentRegistration.this, DisplayQR.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("rollNumber", rollNumber);
                        bundle.putString("studentName", studentName.getText().toString());
                        bundle.putString("motherName", motherName.getText().toString());
                        bundle.putString("email", email.getText().toString());
                        bundle.putString("studentContact", studentContactNumber.getText().toString());
                        bundle.putString("parentsNumber", parentsContactNumber.getText().toString());
                        bundle.putString("fatherName", fatherName.getText().toString());
                        bundle.putString("address", address.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(StudentRegistration.this, "Please Enter Valid Details....", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(StudentRegistration.this, "ALL Fields Are Required....", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void registerStudent() {
        simpleProgressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.REGISTER_STUDENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    boolean isTrue = object.getBoolean("isSuccessful");
                    simpleProgressBar.setVisibility(View.GONE);
                    if (isTrue == true) {
                        Intent intent = new Intent(StudentRegistration.this, DisplayQR.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("rollNumber", object.getString("rollNumber"));
                        bundle.putString("studentName", object.getString("studentName"));
                        bundle.putString("motherName", object.getString("motherName"));
                        bundle.putString("email", object.getString("email"));
                        bundle.putString("studentContact", object.getString("studentContact"));
                        bundle.putString("parentsNumber", object.getString("parentsNumber"));
                        bundle.putString("fatherName", object.getString("fatherName"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        Toast.makeText(StudentRegistration.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentRegistration.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("studentName", studentName.getText().toString());
                hashMap.put("motherName", motherName.getText().toString());
                hashMap.put("address", address.getText().toString());
                hashMap.put("contactNumber", studentContactNumber.getText().toString());
                hashMap.put("parentsNumber", parentsContactNumber.getText().toString());
                hashMap.put("email", email.getText().toString());
                hashMap.put("rollNumber", rollNumber);
                 hashMap.put("fatherName", fatherName.getText().toString());
                Log.d("hashMap", hashMap.toString());
                return hashMap;

            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentRegistration.this);
        requestQueue.add(request);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_student_list:
                // Action goes here
                Toast.makeText(SuperAdmin.this, "student list", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_log_out:
                Toast.makeText(SuperAdmin.this, "LogOut", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}