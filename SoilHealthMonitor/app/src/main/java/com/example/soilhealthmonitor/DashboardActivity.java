package com.example.soilhealthmonitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soilhealthmonitor.Response.ListResponse;
import com.example.soilhealthmonitor.apiconfig.ApiClient;
import com.example.soilhealthmonitor.apiconfig.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    RecyclerView recyclerView;

    ListResponse listResponse;
    Context context;
    UserListAdapter userListAdapter;
    ArrayList<ListResponse.UserInformation> al_user_info;

    Toolbar toolbar;

    SharedPreferences sharedPreferences;
    String ADMIN_LOGIN="ADMINLOGIN";
    String flag;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context=DashboardActivity.this;
        progressDialog=new ProgressDialog(context);

        sharedPreferences=getSharedPreferences(ADMIN_LOGIN,MODE_PRIVATE);
        flag=sharedPreferences.getString("flag","");
        Log.d(TAG, "onCreate:admin_flag dashboard "+flag);

        recyclerView=findViewById(R.id.recycler_users);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Dashboard");
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        list_users();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_logout:
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("flag","0");
                editor.apply();
                editor.commit();
                startActivity(new Intent(context,LoginActivity.class));
                finish();
               // Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;

            case R.id.item_add_user:
                startActivity(new Intent(context,UserRegistrationActivity.class));
                break;
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void list_users() {
        if (isOnline())
        {
            progressDialog.setMessage("Loading wait..!!");
            progressDialog.show();
            ApiInterface apiInterface= ApiClient.getRetrofitInstance().create(ApiInterface.class);
            Call<ListResponse> listResponseCall=apiInterface.list_user();
            listResponseCall.enqueue(new Callback<ListResponse>() {
                @Override
                public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                    listResponse=response.body();
                    progressDialog.dismiss();
                    if (listResponse.getStatus())
                    {
                        al_user_info=new ArrayList<>();
                        for (int i = 0; i < listResponse.getUserInformation().size() ; i++) {
                            al_user_info.add(listResponse.getUserInformation().get(i));
                        }

                        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
                        recyclerView.addItemDecoration(dividerItemDecoration);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));

                        userListAdapter= new UserListAdapter(context,al_user_info);

                        recyclerView.setAdapter(userListAdapter);

                    }
                }

                @Override
                public void onFailure(Call<ListResponse> call, Throwable t) {
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