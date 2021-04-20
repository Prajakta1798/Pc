package com.example.soilhealthmonitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.soilhealthmonitor.Response.EditResponse;
import com.example.soilhealthmonitor.apiconfig.ApiClient;
import com.example.soilhealthmonitor.apiconfig.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    String name,id,mbno,area,password;
    String TAG="Edit";
    EditText etname,etmbno,etarea,etpass;
    Button btnsub;
    EditResponse editResponse;
    Toolbar toolbar;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        progressDialog=new ProgressDialog(this);

        Bundle bundle=getIntent().getExtras();
        name=bundle.getString("name");
        id=bundle.getString("id");
        mbno=bundle.getString("mbno");
        area=bundle.getString("city");
        password=bundle.getString("password");
        Log.d(TAG, "onCreate: id "+id+" name "+name+" mb no "+mbno+" area "+area+" password "+password);

        etname=findViewById(R.id.et_name);
        etmbno=findViewById(R.id.et_mb_no);
        etarea=findViewById(R.id.et_area);
        etpass=findViewById(R.id.et_password);
        btnsub=findViewById(R.id.btn_submit);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Details");
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String[] name_array=name.split(": ");
        String[] mbno_array=mbno.split(": ");
        String[] area_array=area.split(": ");

        etname.setText(name_array[1]);
        etmbno.setText(mbno_array[1]);
        etarea.setText(area_array[1]);
        etpass.setText(password);

        btnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strname=etname.getText().toString();
                String strmbno=etmbno.getText().toString();
                String strarea=etarea.getText().toString();
                String strpass=etpass.getText().toString();
                Log.d(TAG, "onClick: updated value name  "+strname+" mb no "+strmbno+" area "+strarea);

                edit_user(strname,strmbno,strarea,strpass);
            }
        });




    }

    private void edit_user(String strname, String strmbno, String strarea, String strpass) {
        if (isOnline())
        {
            progressDialog.setMessage("Loading wait..!!");
            progressDialog.show();
            ApiInterface apiInterface= ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<EditResponse> editResponseCall=apiInterface.edit_user(strname,strmbno,strpass,strarea,id);

            editResponseCall.enqueue(new Callback<EditResponse>() {
                @Override
                public void onResponse(Call<EditResponse> call, Response<EditResponse> response) {
                    progressDialog.dismiss();
                    editResponse=response.body();
                    if(editResponse.getStatus())
                    {
                        Toast.makeText(EditActivity.this, editResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(EditActivity.this, editResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<EditResponse> call, Throwable t) {
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