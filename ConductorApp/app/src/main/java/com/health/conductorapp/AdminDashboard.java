package com.health.conductorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminDashboard extends AppCompatActivity {
    CardView studentRegistration, conductorRegistration, studentList,cardAttendanceStatus,cardConductrList;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        studentRegistration = findViewById(R.id.card_student);
        conductorRegistration = findViewById(R.id.card_conductor);
        studentList = findViewById(R.id.card_student_list);
        cardAttendanceStatus = findViewById(R.id.card_attendance_status);
        cardConductrList = findViewById(R.id.card_conductor_list);
        logout=findViewById(R.id.id_logout);

        studentRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, StudentRegistration.class);
                startActivity(intent);

            }
        });

        conductorRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, ConductorRegistration.class);
                startActivity(intent);
            }
        });

        studentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, StudentList.class);
                startActivity(intent);

            }
        });

        cardAttendanceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this,AttendanceStatus.class);
                startActivity(intent);

            }
        });

        cardConductrList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this,ConductorList.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this,LoginActivity.class));
                finish();
            }
        });

    }
}