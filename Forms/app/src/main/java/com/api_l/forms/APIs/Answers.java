package com.api_l.forms.APIs;

import com.api_l.forms.Models.AnswerModel;
import com.api_l.forms.Models.AuthModel;
import com.api_l.forms.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Answers {
    @POST("api/answers/")
    public Call<AnswerModel> AddAnswer(@Body AnswerModel body);


}
