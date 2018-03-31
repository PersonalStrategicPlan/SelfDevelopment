package com.api_l.forms.APIs;

import com.api_l.forms.Clients.RetrofitClient;

public class ApiUtils {
    public static final String BASE_URL = "http://api-l.com/FormApi/";


    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
    public static FormServices getForms(){
        return RetrofitClient.getClient(BASE_URL).create(FormServices.class);
    }
    public static DomainServices getDomains(){
        return RetrofitClient.getClient(BASE_URL).create(DomainServices.class);
    }

    public static GoalServices getGoals() {
        return RetrofitClient.getClient(BASE_URL).create(GoalServices.class);

    }
    public  static AnswerServices getAnswers(){
        return RetrofitClient.getClient(BASE_URL).create(AnswerServices.class);
    }
    public  static TaqeemServices Taqeem(){
        return RetrofitClient.getClient(BASE_URL).create(TaqeemServices.class);
    }

}
