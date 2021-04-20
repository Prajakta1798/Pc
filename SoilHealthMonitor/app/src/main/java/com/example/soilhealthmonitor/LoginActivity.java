package com.example.soilhealthmonitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soilhealthmonitor.Response.LoginResponse;
import com.example.soilhealthmonitor.apiconfig.ApiClient;
import com.example.soilhealthmonitor.apiconfig.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG =LoginActivity.class.getName() ;
    String strusername, strpassword;
    Context context;
    ProgressDialog progressDialog;

    EditText etusername, etpassword;
    Button btnlogin;
    ImageView imageAdmin;
    LoginResponse loginResponse;
    SharedPreferences sharedPreferences;
    String USER_LOGIN="USERLOGIN";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        progressDialog=new ProgressDialog(context);

        sharedPreferences=getSharedPreferences(USER_LOGIN,MODE_PRIVATE);


        etusername = (EditText) findViewById(R.id.et_username);//for xml unique id
        etpassword = (EditText) findViewById(R.id.et_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        imageAdmin = (ImageView) findViewById(R.id.image_admin);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strusername = etusername.getText().toString();
                strpassword = etpassword.getText().toString();

             /*   if (! strusername.matches("Admin")) {
                    etusername.setError("Enter Username");
                    etusername.requestFocus();
                    return;
                }
                if (!strpassword.matches("Admin")) {
                    etpassword.setError("Enter Password");
                    etpassword.requestFocus();
                    return;
                }*/
                if (TextUtils.isEmpty(strusername) || TextUtils.isEmpty(strpassword))
                {
                    Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show();

                }else {
                    Log.d(TAG, "onClick: "+"button clicked");

                    login_user(strusername,strpassword);


                }
            }
        });

        imageAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,AdminLoginActivity.class));
                finish();
            }
        });
    }

    private void login_user(String strusername, String strpassword) {

        if (isOnline()) //phone is connected to net or not
        {
            progressDialog.setMessage("Loading wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);//php database values for connectivity
            Call<LoginResponse> loginResponseCall = apiInterface.login_user(strusername, strpassword);
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    loginResponse = response.body();
                    progressDialog.dismiss();

                    Log.d(TAG, "onResponse:Login response "+loginResponse.toString());
                    Log.d(TAG, "onResponse:Login response "+loginResponse.getStatus());
                    if (loginResponse.getStatus())
                    {
                        Log.d(TAG, "onResponse:Login "+loginResponse.getMsg());
                        Toast.makeText(LoginActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("flag","2");
                        editor.apply();
                        editor.commit();


                        startActivity(new Intent(context, UserDashboardActivity.class)
                                /* .putExtra("flag","1")*/);
                        finish();

                    }
                    else
                    {
                        Log.d(TAG, "onResponse:Login "+loginResponse.getMsg());

                        Toast.makeText(LoginActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onFailure: "+t.toString());
                }
            });
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}