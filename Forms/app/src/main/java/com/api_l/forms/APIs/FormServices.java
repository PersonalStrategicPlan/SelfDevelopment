package com.api_l.forms.APIs;

import com.api_l.forms.Models.AuthModel;
import com.api_l.forms.Models.FormModel;
import com.api_l.forms.Models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;



public interface FormServices {
    @GET("api/forms/")
    public Call<List<FormModel>> GetAllForms();
}
