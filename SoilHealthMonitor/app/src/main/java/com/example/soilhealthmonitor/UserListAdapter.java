package com.example.soilhealthmonitor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soilhealthmonitor.Response.DeleteResponse;
import com.example.soilhealthmonitor.Response.ListResponse;
import com.example.soilhealthmonitor.apiconfig.ApiClient;
import com.example.soilhealthmonitor.apiconfig.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*extends RecyclerView.Adapter<ElectricityRecyclerAdapter.NotificationViewHolder>*/
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    Context context;
    ArrayList<ListResponse.UserInformation> al_user_list;
    DeleteResponse deleteResponse;
    ProgressDialog progressDialog;


    public UserListAdapter(Context context, ArrayList<ListResponse.UserInformation> al_user_info) {
        this.al_user_list = al_user_info;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.users_list, parent, false);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        if (al_user_list != null) {
            progressDialog=new ProgressDialog(context);
            holder.tvUserName.setText("Name : "+al_user_list.get(position).getName());
            holder.tvUserCity.setText("Area : "+al_user_list.get(position).getArea());
            holder. tvUserMbNo.setText("Mb No : "+al_user_list.get(position).getMobile());
            holder. tvUserId.setText(al_user_list.get(position).getId());
            holder. tvUserPass.setText(al_user_list.get(position).getPassword());

            holder.imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String userid=holder.tvUserId.getText().toString();
                    Log.d("TAG", "onClick:userid "+userid);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage("Are you sure you want to delete user ?").setTitle("Delete User");

                    //Setting message manually and performing action on button click
                    builder.setMessage("Are you sure you want to delete user ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // context.finish();
                                 //   Toast.makeText(context, "you choose yes action for alertbox", Toast.LENGTH_SHORT).show();
                                    if (isOnline()) {
                                        progressDialog.setMessage("Loading Wait..!!");
                                        progressDialog.show();
                                        ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
                                        Call<DeleteResponse> deleteResponseCall=apiInterface.delete_user(userid);
                                        deleteResponseCall.enqueue(new Callback<DeleteResponse>() {
                                            @Override
                                            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                                                progressDialog.dismiss();
                                                deleteResponse=response.body();
                                                Log.d("TAG", "onResponse:delete user "+response.body());
                                                if (deleteResponse.getStatus())
                                                {
                                                    Toast.makeText(context, deleteResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(context, deleteResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(context, "you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Delete User");
                    alert.show();
                }
            });


            holder.imageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,EditActivity.class)
                    .putExtra("name",holder.tvUserName.getText().toString())
                    .putExtra("id",holder.tvUserId.getText().toString())
                    .putExtra("mbno",holder.tvUserMbNo.getText().toString())
                    .putExtra("city",holder.tvUserCity.getText().toString())
                    .putExtra("password",holder.tvUserPass.getText().toString())
                    );
                }
            });

        }
            /*});
        }*/
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public int getItemCount() {
        return al_user_list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName;

        ImageView imageDelete;

        ImageView imageEdit;

        TextView tvUserMbNo;

        TextView tvUserCity;

        TextView tvUserId;
        TextView tvUserPass;

        public UserViewHolder(@NonNull View itemView) {
           super(itemView);

           tvUserName=itemView.findViewById(R.id.tv_user_name);
            imageDelete=itemView.findViewById(R.id.image_delete);
            imageEdit=itemView.findViewById(R.id.image_edit);
            tvUserMbNo=itemView.findViewById(R.id.tv_user_mb_no);
            tvUserCity=itemView.findViewById(R.id.tv_user_city);
            tvUserId=itemView.findViewById(R.id.tv_user_id);
            tvUserPass=itemView.findViewById(R.id.tv_user_password);

           /* super(itemView);
            ButterKnife.bind(context,itemView);*/


        }
    }
}
