package com.api_l.forms.APIs;

import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.TaqeemModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface TaqeemServices {
    @GET("api/Taqeem")
    public Call<TaqeemModel> GetDomainTaqeem(@Query("userid") int id, @Query("domainId") int domainid);
    @GET("api/formpercentage")
    public Call<TaqeemModel> GetFormTaqeem(@Query("userid") int id, @Query("formId") int domainid);

}
