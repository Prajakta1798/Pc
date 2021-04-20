package com.example.soilhealthmonitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soilhealthmonitor.Response.RegistrationResponse;
import com.example.soilhealthmonitor.apiconfig.ApiClient;
import com.example.soilhealthmonitor.apiconfig.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG ="TAG" ;

    EditText etName;

    EditText etPassword;

    EditText etMbNo;

    EditText etArea;

    Button btnRegister;


    String strname,strpass,strmbno,strarea;
    RegistrationResponse registrationResponse;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        progressDialog=new ProgressDialog(this);

        etName=findViewById(R.id.et_name);
        etPassword=findViewById(R.id.et_password);
        etMbNo=findViewById(R.id.et_mb_no);
        etArea=findViewById(R.id.et_area);
        btnRegister=findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strname=etName.getText().toString();
                strarea=etArea.getText().toString();
                strmbno=etMbNo.getText().toString();
                strpass=etPassword.getText().toString();
                Log.d(TAG, "onViewClicked: name "+strname+" area :"+strarea+" mb no"+strmbno+" pass "+strpass);
                if (TextUtils.isEmpty(strname))
                {
                    etName.setError("Enter name");
                    etName.requestFocus();
                    return;
                } if (TextUtils.isEmpty(strarea))
                {
                    etArea.setError("Enter Area");
                    etArea.requestFocus();
                    return;
                } if (TextUtils.isEmpty(strmbno))
                {
                    etMbNo.setError("Enter Mobile No");
                    etMbNo.requestFocus();
                    return;
                } if (TextUtils.isEmpty(strpass))
                {
                    etPassword.setError("Enter Password");
                    etPassword.requestFocus();
                    return;
                }
                else
                {
                    if (isOnline())
                    {
                        progressDialog.setMessage("Loading Wait..!!");
                        progressDialog.show();
                        ApiInterface apiInterface= ApiClient.getRetrofitInstance().create(ApiInterface.class);
                        Call<RegistrationResponse> registrationResponseCall=apiInterface.register_user(strname,strmbno,strpass,strarea);
                        registrationResponseCall.enqueue(new Callback<RegistrationResponse>() {
                            @Override
                            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                                registrationResponse=response.body();
                                progressDialog.dismiss();
                                if (registrationResponse.getStatus())
                                {
                                    Toast.makeText(UserRegistrationActivity.this, registrationResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(UserRegistrationActivity.this, registrationResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.d(TAG, "onFailure: "+t.toString());
                            }
                        });

                    }

                }
            }
        });
      //  ButterKnife.bind(this);
    }

  /*  @OnClick(R.id.btn_register)
    public void onViewClicked() {

    }*/



    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}