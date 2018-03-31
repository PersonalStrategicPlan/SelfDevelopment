package com.api_l.forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.Auth;
import com.api_l.forms.Models.AuthModel;
import com.api_l.forms.Models.UserModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Auth authService;
    private Button loginButn;
    private EditText username, userpass;
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButn = (Button) findViewById(R.id.loginBtn);
        username = (EditText) findViewById(R.id.userName);
        userpass = (EditText) findViewById(R.id.userPass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        authService = ApiUtils.getUserService();
        loginButn.setOnClickListener(this);
        showProgress(false);

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
            showProgress(false);
            if(loggedInUser!=null) {
                if(loggedInUser.getUserFullName() == null){
                    Toast.makeText(LoginActivity.this, "حاول مرة أخرى هناك مشكلة بالاتصال", Toast.LENGTH_LONG).show();
                }
              //  Log.d("OBJECT: ", loggedInUser.getUserFullName());
                else{
                    Toast.makeText(LoginActivity.this, "مرحبا بك " + loggedInUser.getUserFullName(), Toast.LENGTH_LONG).show();

                    sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putInt("UserId",loggedInUser.getUserid());
                    editor.putString("UserFullName",loggedInUser.getUserFullName());
                    editor.commit();


               Move();
                }
            } else {
                Toast.makeText(LoginActivity.this, "فضلاً تأكد من بياناتك المدخلة", Toast.LENGTH_LONG).show();
            }
        }



        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }
    private void Move() {
        Intent toFormsActivit = new Intent(this,FormsActivity.class);
        startActivity(toFormsActivit);
    }
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }

    public void doLogin(String userEmail, String userPass) {
        showProgress(true);
        AuthModel userModel = new AuthModel(userEmail, userPass);
        Call call = authService.Authenticate(userModel);
        new UserLoginTask().execute(call);

    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginBtn){
            doLogin(username.getText().toString(),userpass.getText().toString());
        }
    }
}
