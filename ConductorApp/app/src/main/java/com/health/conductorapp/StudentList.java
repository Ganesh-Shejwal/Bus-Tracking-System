package com.health.conductorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentList extends AppCompatActivity {
    private RecyclerView rvStudentList;
    ArrayList<HashMap<String, String>> studentList;
    StudentListAdapter studentListAdapter;
    ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        studentList = new ArrayList<>();
        rvStudentList = findViewById(R.id.rv_student_list);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rvStudentList.setLayoutManager(layoutManager);
        rvStudentList.setHasFixedSize(true);
        getStudentList();

    }


    private void getStudentList() {
        studentList.clear();
        simpleProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.STUDENT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("orderResponse", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("ResponseMessage");
                    simpleProgressBar.setVisibility(View.GONE);
                    if (code.equals("1")) {
                        Toast.makeText(StudentList.this, "" + msg, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray("studentList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> hm = new HashMap<>();
                            JSONObject object = jsonArray.getJSONObject(i);
                            hm.put("roll_number", object.getString("roll_number"));
                            hm.put("student_name", object.getString("student_name"));
                            hm.put("father_name", object.getString("father_name"));
                            hm.put("mother_name", object.getString("mother_name"));
                            hm.put("address", object.getString("address"));
                            hm.put("student_mob", object.getString("student_mob"));
                            hm.put("parents_mob", object.getString("parents_mob"));
                            hm.put("student_email", object.getString("student_email"));
                            hm.put("image_path", object.getString("image_path"));
                            studentList.add(hm);


                        }

                        studentListAdapter = new StudentListAdapter(StudentList.this, studentList, StudentList.this);
                        rvStudentList.setAdapter(studentListAdapter);
                        studentListAdapter.notifyDataSetChanged();


                    } else {
                        simpleProgressBar.setVisibility(View.GONE);
                        Toast.makeText(StudentList.this, "" + msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                    simpleProgressBar.setVisibility(View.GONE);
                    Log.i("exception", "" + e);
                    Toast.makeText(StudentList.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                simpleProgressBar.setVisibility(View.GONE);
                Toast.makeText(StudentList.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(StudentList.this);
        requestQueue.add(stringRequest);

    }

    public void callApi()
    {
        getStudentList();
    }

    public void deleteStudent(String rollNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_STUDENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("responseData", "" + response);

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("responseMessage");
                    if (code.equals("1")) {
                        Toast.makeText(StudentList.this, "" + msg, Toast.LENGTH_SHORT).show();
                        getStudentList();

                    } else {
                        Toast.makeText(StudentList.this, "" + msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("exception", "" + e);
                    Toast.makeText(StudentList.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                Toast.makeText(StudentList.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("rollNumber", rollNumber);
                return hashMap;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(StudentList.this);
        requestQueue.add(stringRequest);

    }
}