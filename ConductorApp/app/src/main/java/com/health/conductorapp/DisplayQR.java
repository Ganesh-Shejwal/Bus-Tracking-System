package com.health.conductorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DisplayQR extends AppCompatActivity {
    private ImageView qrCodeIV;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String rollNumber, studentName, motherName, email, studentContact, parentsNumber, fatherName, address;
    ProgressBar simpleProgressBar;
    int flag = 0;

    RelativeLayout backToHome;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr);
        qrCodeIV = findViewById(R.id.idIVQrcode);
        backToHome = findViewById(R.id.back_to_home_relative);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rollNumber = bundle.getString("rollNumber");
            studentName = bundle.getString("studentName");
            motherName = bundle.getString("motherName");
            email = bundle.getString("email");
            studentContact = bundle.getString("studentContact");
            parentsNumber = bundle.getString("parentsNumber");
            fatherName = bundle.getString("fatherName");
            address = bundle.getString("address");


            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int dimen = width < height ? width : height;
            dimen = dimen * 3 / 4;
            qrgEncoder = new QRGEncoder(rollNumber, null, QRGContents.Type.TEXT, dimen);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrCodeIV.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.e("Tag", e.toString());
            }

        }
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(DisplayQR.this, StudentRegistration.class);
                // startActivity(intent);
                // finish();

                if (flag == 0) {
                    // registerStudent();
                    uploadImage();

                } else {
                    Toast.makeText(DisplayQR.this, "You are already register....", Toast.LENGTH_SHORT).show();
                    //finish();
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
                        flag = 1;
                        Toast.makeText(DisplayQR.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(DisplayQR.this, "" + object.getString("ResponseMessage"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("studentName", studentName);
                hashMap.put("motherName", motherName);
                hashMap.put("address", address);
                hashMap.put("contactNumber", studentContact);
                hashMap.put("parentsNumber", parentsNumber);
                hashMap.put("email", email);
                hashMap.put("rollNumber", rollNumber);
                hashMap.put("fatherName", fatherName);
                hashMap.put("qrCode", String.valueOf(bitmap));
                Log.d("hashMap", hashMap.toString());
                return hashMap;

            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(DisplayQR.this);
        requestQueue.add(request);

    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void uploadImage() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetimeName = ft.format(dNow);
        Log.i("datetime", "" + datetimeName);


        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DisplayQR.this, "Please wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                finish();
                // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.FROYO)
            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                HashMap<String, String> data = new HashMap<>();
                data.put("studentName", studentName);
                data.put("motherName", motherName);
                data.put("address", address);
                data.put("contactNumber", studentContact);
                data.put("parentsNumber", parentsNumber);
                data.put("email", email);
                data.put("rollNumber", rollNumber);
                data.put("fatherName", fatherName);
                data.put("image_path", uploadImage);
                data.put("image_name", datetimeName);
                Log.d("hashMap", data.toString());
                String result = rh.sendPostRequest(Urls.REGISTER_STUDENT, data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}
