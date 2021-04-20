package com.example.soilhealthmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    public static int SPLASH_TIME_OUT = 4000;
    Context mContext;

        //session maintain
    SharedPreferences sharedPreferences;
    String ADMIN_LOGIN="ADMINLOGIN";
    String flag1,flag;

    SharedPreferences sharedPreferencesuser;
    String USER_LOGIN="USERLOGIN";


    //  private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences=getSharedPreferences(ADMIN_LOGIN,MODE_PRIVATE);
        flag1=sharedPreferences.getString("flag","");
        Log.d(TAG, "onCreate:admin_flag "+flag1);

        sharedPreferencesuser=getSharedPreferences(USER_LOGIN,MODE_PRIVATE);
        flag=sharedPreferencesuser.getString("flag","");
        Log.d(TAG, "onCreate:user_flag "+flag);



        mContext=SplashActivity.this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: admin "+flag1+" user "+flag);
                if (flag1.matches("1"))
                {
                    startActivity(new Intent(mContext, DashboardActivity.class));
                    finish();
                }
               else if (flag.matches("2"))
                {
                    startActivity(new Intent(mContext, UserDashboardActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

    }
}