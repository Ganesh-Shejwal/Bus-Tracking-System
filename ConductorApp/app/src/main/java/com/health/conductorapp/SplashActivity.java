package com.health.conductorapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashfile);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isInternet(SplashActivity.this)) {
                   Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                   // Intent intent = new Intent(SplashActivity.this, SuperAdmin.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }


    public boolean isInternet(final Activity context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("OOP'S Connection Error");
            builder.setMessage("Please check your internet connection!");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {

                    ActivityCompat.finishAffinity(SplashActivity.this);
                    finish();

                }
            });/*.create().show();*/

            androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.show();
            // alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);


            return false;
        }

        return true;
    }
}