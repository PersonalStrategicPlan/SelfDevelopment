package com.api_l.forms.APIs;

import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.GoalModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;



public interface Goals {
    @GET("api/goals/{id}")
    public Call<List<GoalModel>> GetAllDomainGoals(@Path("id") int id);
}
