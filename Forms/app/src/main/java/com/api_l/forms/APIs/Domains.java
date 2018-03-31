package com.api_l.forms.APIs;

import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.FormModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface Domains {
    @GET("api/domains/")
    public Call<List<DomainModel>> GetAllDomains();

}
