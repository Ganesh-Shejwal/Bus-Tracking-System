package com.health.conductorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AttendanceStatus extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvAttendanceStatus;
    private StudentAttendanceAdapter studentAttendanceAdapter;
    ArrayList<HashMap<String, String>> attendanceStatusList;
    EditText selectdate;
    ProgressBar simpleProgressBar;
    private int mYear, mMonth, mDay;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_status);
        selectdate = findViewById(R.id.edtdate);

        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);
        System.out.println(date);
        selectdate.setText("" + date);

        selectdate.setOnClickListener(this);
        attendanceStatusList = new ArrayList<>();
        rvAttendanceStatus = findViewById(R.id.rv_attendance_status);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rvAttendanceStatus.setLayoutManager(layoutManager);
        rvAttendanceStatus.setHasFixedSize(true);
        getAttendancestatus(selectdate.getText().toString());

    }


    private void getAttendancestatus(String date) {
        attendanceStatusList.clear();
        simpleProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.STUDENT_ATTENDANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("responseData", "" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("ResponseMessage");
                    simpleProgressBar.setVisibility(View.GONE);
                    if (code.equals("1")) {
                        rvAttendanceStatus.setVisibility(View.VISIBLE);
                        Toast.makeText(AttendanceStatus.this, "" + msg, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray("studentAttendanceList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> hm = new HashMap<>();
                            JSONObject object = jsonArray.getJSONObject(i);
                            hm.put("roll_number", object.getString("roll_number"));
                            hm.put("student_name", object.getString("student_name"));
                            hm.put("status", object.getString("status"));
                            hm.put("attendance_date", object.getString("attendance_date"));
                            attendanceStatusList.add(hm);


                        }

                        studentAttendanceAdapter = new StudentAttendanceAdapter(AttendanceStatus.this, attendanceStatusList);
                        rvAttendanceStatus.setAdapter(studentAttendanceAdapter);
                        studentAttendanceAdapter.notifyDataSetChanged();


                    } else {
                        simpleProgressBar.setVisibility(View.GONE);
                        rvAttendanceStatus.setVisibility(View.GONE);
                        Toast.makeText(AttendanceStatus.this, "" + msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    simpleProgressBar.setVisibility(View.GONE);
                    Log.i("exception", "" + e);
                    Toast.makeText(AttendanceStatus.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                simpleProgressBar.setVisibility(View.GONE);
                Toast.makeText(AttendanceStatus.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("selectedDate", date);
                return hashMap;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceStatus.this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onClick(View v) {
        if (v == selectdate) {
            final Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            //show dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // selectdate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    month += 1;
                    String mt, dy;
                    if (month < 10)
                        mt = "0" + month; //if month less than 10 then ad 0 before month
                    else
                        mt = String.valueOf(month);

                    if (dayOfMonth < 10)
                        dy = "0" + dayOfMonth;
                    else
                        dy = String.valueOf(dayOfMonth);

                    selectdate.setText(year + "-" + mt + "-" + dy);
                    //selectdate.setText(year + "-" + month+1 + "-" + dayOfMonth);
                    getAttendancestatus(selectdate.getText().toString());
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}