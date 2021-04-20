package com.example.soilhealthmonitor;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    Button btnAddUser;
    Button btnGetDetails;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        context=AdminActivity.this;

        btnAddUser=findViewById(R.id.btn_add_user);
        btnGetDetails=findViewById(R.id.btn_get_details);
    }
}