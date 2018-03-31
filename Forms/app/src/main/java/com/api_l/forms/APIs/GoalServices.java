package com.api_l.forms.APIs;

import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.Models.TaqeemModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface GoalServices
{
    @GET("api/goals/")
    public Call<List<GoalModel>> GetAllGoals(@Query("formid") Integer formid,@Query("domainid") Integer domainid,  @Query("userid") Integer userid,@Query("isDomain") boolean isdomain);


    @POST("api/goals/")
    public Call<GoalModel> AddNewGoal(@Body GoalModel goalModel);

    @PUT("api/goals/")
    public Call<GoalModel> UpdateGoal(@Body GoalModel goalModel);

    @DELETE("api/goals/")
    public Call<GoalModel> DeleteGoal(@Query("goalId")Integer goalId);


}
