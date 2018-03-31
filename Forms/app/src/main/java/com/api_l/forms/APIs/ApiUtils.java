package com.api_l.forms.APIs;

import com.api_l.forms.Clients.RetrofitClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {


    public static final String BASE_URL = "http://api-l.com/FormApi/";


    public static Auth getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(Auth.class);
    }
    public static Forms getForms(){
        return RetrofitClient.getClient(BASE_URL).create(Forms.class);
    }
    public static Domains getDomains(){
        return RetrofitClient.getClient(BASE_URL).create(Domains.class);
    }
}
