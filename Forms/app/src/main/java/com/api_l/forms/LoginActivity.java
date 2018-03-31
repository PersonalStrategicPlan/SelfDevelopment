package com.api_l.forms;

import android.content.Intent;
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

    @Override
    //utilizing variabuls
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
            try {
                Response<UserModel> responseBody = c.execute();
                UserModel loggedInUser =responseBody.body();
                return  loggedInUser;
            } catch (IOException e) {
                Toast.makeText(LoginActivity.this, "Try again please!", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(UserModel  loggedInUser) {
            showProgress(false);
            if(loggedInUser!=null) {
                Log.d("OBJECT: ", loggedInUser.getUserFullName());
                Toast.makeText(LoginActivity.this, "مرحبا بك " + loggedInUser.getUserFullName(), Toast.LENGTH_LONG).show();
               // Move();
            }else {
                Toast.makeText(LoginActivity.this, "Not Found", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }

    //move from page to page using intent class
    private void Move() {
        Intent toFormsActivit = new Intent(this,FormsActivity.class);
        startActivity(toFormsActivit);
    }

    //loader for loading icon(hide or visible)
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);
    }

    //doing login (username, password)
    public void doLogin(String userName, String userPass) {
        showProgress(true);
        AuthModel userModel = new AuthModel(userName, userPass);
        Call call = authService.Authenticate(userModel);
        new UserLoginTask().execute(call);

    }

    //when press enter class login method
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginBtn){
            doLogin(username.getText().toString(),userpass.getText().toString());
        }
    }
}
