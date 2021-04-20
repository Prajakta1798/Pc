package com.example.soilhealthmonitor.apiconfig;



import com.example.soilhealthmonitor.Response.DeleteResponse;
import com.example.soilhealthmonitor.Response.EditResponse;
import com.example.soilhealthmonitor.Response.ListResponse;
import com.example.soilhealthmonitor.Response.LoginResponse;
import com.example.soilhealthmonitor.Response.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface
{

    @GET("login.php")
    Call<LoginResponse> login_user(@Query("mobile") String mobile, @Query("password") String password);

    @GET("list.php")
    Call<ListResponse> list_user();

    @GET("delete.php")
    Call<DeleteResponse> delete_user(@Query("id") String id);

    @GET("registration.php")
    Call<RegistrationResponse> register_user(@Query("name") String name, @Query("mobile") String mobile, @Query("password") String password, @Query("area") String area);

    @GET("edit.php")
    Call<EditResponse> edit_user(@Query("name") String name, @Query("mobile") String mobile, @Query("password") String password, @Query("area") String area, @Query("id") String id);

}
