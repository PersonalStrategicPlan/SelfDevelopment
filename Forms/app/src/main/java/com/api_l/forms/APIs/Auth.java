package com.api_l.forms.APIs;

import com.api_l.forms.Models.AuthModel;
import com.api_l.forms.Models.UserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;



public interface Auth {


   // @Header("application/json; charset=utf-8")
    @POST("api/authinticate/")
    public Call<UserModel> Authenticate(@Body AuthModel body);


}

