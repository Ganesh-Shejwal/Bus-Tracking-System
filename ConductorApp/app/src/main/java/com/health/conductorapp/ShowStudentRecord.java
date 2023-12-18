package com.health.conductorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShowStudentRecord extends AppCompatActivity {
    TextView todaysDate, studentName, studentRollNumber, motherName, studentContactNumber, parentsContactNumber, studentEmail, attendanceStatus;
    LinearLayout leaniarButton;
    String rollNumber;
    ProgressBar simpleProgressBar;
    int flag = 0;
    String fathetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        todaysDate = findViewById(R.id.txt_date);
        studentName = findViewById(R.id.txt_student_name);
        studentRollNumber = findViewById(R.id.txt_roll_number);
        motherName = findViewById(R.id.txt_mother_name);
        studentContactNumber = findViewById(R.id.txt_student_contact);
        parentsContactNumber = findViewById(R.id.txt_parents_contact);
        studentEmail = findViewById(R.id.txt_student_email);
        leaniarButton = findViewById(R.id.linear_button);
        attendanceStatus = findViewById(R.id.attendance_status);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        //  SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        Log.i("formatedDate", "" + formattedDate);

        todaysDate.setText("" + formattedDate);

        Intent intent = getIntent();
        if (intent != null) {
            rollNumber = intent.getStringExtra("rollNumber");
            getStudentRecord();

        }

        attendanceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    saveAttendance();
                }

            }
        });


    }

    private void getStudentRecord() {

        simpleProgressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.GET_STUDENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("studentRespo", response);

                try {
                    JSONObject object = new JSONObject(response);
                    boolean isTrue = object.getBoolean("isSuccessful");

                    if (isTrue == true) {
                        studentName.setText("" + object.getString("studentName"));
                        motherName.setText("" + object.getString("motherName"));
                        studentRollNumber.setText("" + object.getString("rollNumber"));
                        studentContactNumber.setText("" + object.getString("studentContact"));
                        studentEmail.setText("" + object.getString("email"));
                        parentsContactNumber.setText("" + object.getString("parentsNumber"));
                        attendanceStatus.setText("" + object.getString("statusResult"));
                        fathetName = object.getString("fatherName");

                        if (object.getString("statusResult").equals("IN")) {
                            leaniarButton.setBackgroundColor(getResources().getColor(R.color.button_color));
                        } else {
                            leaniarButton.setBackgroundColor(getResources().getColor(R.color.color_red));

                        }
                        simpleProgressBar.setVisibility(View.GONE);

                    } else {
                        simpleProgressBar.setVisibility(View.GONE);
                        Toast.makeText(ShowStudentRecord.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    simpleProgressBar.setVisibility(View.GONE);
                    Log.d("error", "" + e);
                    Toast.makeText(ShowStudentRecord.this, "Please try after some time", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                simpleProgressBar.setVisibility(View.GONE);
                Toast.makeText(ShowStudentRecord.this, "Please try after some time", Toast.LENGTH_SHORT).show();


            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("rollNumber", rollNumber);
                hashMap.put("status", attendanceStatus.getText().toString());
                Log.d("hashMap", hashMap.toString());
                return hashMap;

            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(ShowStudentRecord.this);
        requestQueue.add(request);
    }

    private void saveAttendance() {
        flag = 1;
        simpleProgressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.SAVE_ATTENDANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saveResponse", response);
                simpleProgressBar.setVisibility(View.GONE);

                try {
                    JSONObject object = new JSONObject(response);
                    boolean isTrue = object.getBoolean("isSuccessful");
                    if (isTrue == true) {
                        attendanceStatus.setText("OUT");
                        leaniarButton.setBackgroundColor(getResources().getColor(R.color.color_red));
                        Toast.makeText(ShowStudentRecord.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(ShowStudentRecord.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    Log.d("error", "" + e);
                    Toast.makeText(ShowStudentRecord.this, "Please try after some time", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                Toast.makeText(ShowStudentRecord.this, "Please try after some time", Toast.LENGTH_SHORT).show();


            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("rollNumber", rollNumber);
                hashMap.put("studentName", studentName.getText().toString());
                hashMap.put("contactNumber", studentContactNumber.getText().toString());
                hashMap.put("parentsNumber", parentsContactNumber.getText().toString());
                hashMap.put("attendanceStatus", attendanceStatus.getText().toString());
                hashMap.put("attendanceDate", todaysDate.getText().toString());
                hashMap.put("fatherName", fathetName);
                Log.d("hashMap", hashMap.toString());
                return hashMap;

            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(ShowStudentRecord.this);
        requestQueue.add(request);

    }
}