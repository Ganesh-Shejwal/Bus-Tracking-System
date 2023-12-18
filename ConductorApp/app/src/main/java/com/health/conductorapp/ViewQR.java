package com.health.conductorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewQR extends AppCompatActivity {
    ImageView imgQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        imgQrCode = findViewById(R.id.img_qr_code);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        Glide.with(this).load(imageUrl).into(imgQrCode);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}