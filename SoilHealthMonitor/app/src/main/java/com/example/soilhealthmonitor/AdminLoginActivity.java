package com.example.soilhealthmonitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    String strusername, strpassword;
    Context context;
    ProgressDialog progressDialog;

    EditText etusername, etpassword;
    Button btnlogin;
    SharedPreferences sharedPreferences;
    String ADMIN_LOGIN="ADMINLOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        context = AdminLoginActivity.this;
        sharedPreferences=getSharedPreferences(ADMIN_LOGIN,MODE_PRIVATE);

        etusername = (EditText) findViewById(R.id.et_username);
        etpassword = (EditText) findViewById(R.id.et_password);
        btnlogin = (Button) findViewById(R.id.btn_login);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strusername = etusername.getText().toString();
                strpassword = etpassword.getText().toString();

                if (! strusername.matches("admin"))//static username
                {
                    etusername.setError("Enter Username");
                    etusername.requestFocus();
                    return;
                }
                if (!strpassword.matches("admin")) {
                    etpassword.setError("Enter Password");
                    etpassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(strusername) )
                {
                    etusername.setError("Enter Username");
                    etusername.requestFocus();
                    return;
                    //  Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(strpassword))
                {
                    etpassword.setError("Enter Password");
                    etpassword.requestFocus();
                    return;

                }else {
                    Log.d("TAG", "onClick: "+"Inside else ");
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("flag","1");
                    editor.apply();
                    editor.commit();
                    startActivity(new Intent(context, DashboardActivity.class));
                    finish();

                }
            }
        });

    }
}