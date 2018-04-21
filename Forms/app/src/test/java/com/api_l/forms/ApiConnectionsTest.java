package com.api_l.forms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.test.UiThreadTest;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.UserService;
import com.api_l.forms.Models.AuthModel;
import com.api_l.forms.Models.UserModel;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;



public class ApiConnectionsTest {
    private UserModel userModel = null;
    private UserService userServiceService = ApiUtils.getUserService();

    @Test
    public void apiLogin () throws Throwable{


        new Runnable() {
            @Override
            public void run() {
                doLogin("1422818","000000");
            }
        }.run();

        assertEquals(null,userModel);
        }

    public void doLogin(String userEmail, String userPass) {
        AuthModel userModel = new AuthModel(userEmail, userPass);
        Call call = userServiceService.Authenticate(userModel);
        new UserLoginTask().execute(call);

    }
    public class UserLoginTask extends AsyncTask<Call, Void, UserModel> {
        @Override
        protected UserModel doInBackground(Call... params) {
            Call<UserModel> c = params[0];

            Response<UserModel> responseBody = null;
            try {
                responseBody = c.execute();
                UserModel loggedInUser =responseBody.body();
                return  loggedInUser;

            } catch (IOException e) {
                e.printStackTrace();
            }


            return  new UserModel("","");
        }

        @Override
        protected void onPostExecute(UserModel  loggedInUser) {
                  userModel = loggedInUser;

            }
        }

    }

