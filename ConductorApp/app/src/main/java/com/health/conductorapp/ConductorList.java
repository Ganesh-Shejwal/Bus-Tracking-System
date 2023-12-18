package com.health.conductorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConductorList extends AppCompatActivity {
    private RecyclerView rvConductor;
    ArrayList<HashMap<String, String>> conductorList;
    ConductorListAdapter conductorListAdapter;
    private RotateLoading rotateLoading;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_list);
        rvConductor = findViewById(R.id.rv_conductor);
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rvConductor.setLayoutManager(layoutManager);
        rvConductor.setHasFixedSize(true);
        conductorList = new ArrayList<>();
        getConductorList();

      /*  swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        conductorList.clear();
                        getConductorList();
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 2000); // Delay in millis

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/

    }


    public void getConductorList() {
        conductorList.clear();
        rotateLoading.start();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_CONDUCTOR_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("listResponse", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        rotateLoading.stop();
                        JSONArray jsonArray = jsonObject.getJSONArray("conductorList");

                        // JSONObject object = jsonObject.getJSONObject("conductorList");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> hm = new HashMap<>();
                            JSONObject object = jsonArray.getJSONObject(i);
                            hm.put("id", object.getString("id"));
                            hm.put("name", object.getString("name"));
                            hm.put("contactNumber", object.getString("contactNumber"));
                            hm.put("vehicleNumber", object.getString("vehicleNumber"));
                            hm.put("latitude", object.getString("latitude"));
                            hm.put("longitude", object.getString("longitude"));
                            conductorList.add(hm);


                        }
                        conductorListAdapter = new ConductorListAdapter(ConductorList.this, conductorList,ConductorList.this);
                        rvConductor.setAdapter(conductorListAdapter);
                        conductorListAdapter.notifyDataSetChanged();


                    } else {
                        rotateLoading.stop();
                        Toast.makeText(ConductorList.this, "No Conductor found..", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    rotateLoading.stop();
                    Log.i("exception", "" + e);
                    Toast.makeText(ConductorList.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                rotateLoading.stop();
                Toast.makeText(ConductorList.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

            }
        }) {


        };


        RequestQueue requestQueue = Volley.newRequestQueue(ConductorList.this);
        requestQueue.add(stringRequest);

    }


    public void conductorList() {

        getConductorList();
    }


    public void deleteConductor(String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_CONDUCTOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("responseData", "" + response);

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("responseMessage");
                    if (code.equals("1")) {
                        Toast.makeText(ConductorList.this, "" + msg, Toast.LENGTH_SHORT).show();
                        getConductorList();

                    } else {
                        Toast.makeText(ConductorList.this, "" + msg, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("exception", "" + e);
                    Toast.makeText(ConductorList.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "" + error);
                Toast.makeText(ConductorList.this, "PLEASE TRY AFTER SOME TIME", Toast.LENGTH_SHORT).show();

            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", id);
                return hashMap;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(ConductorList.this);
        requestQueue.add(stringRequest);
    }
}