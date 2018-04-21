package com.api_l.forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.UserService;
import com.api_l.forms.Models.AuthModel;
import com.api_l.forms.Models.UserModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private UserService userServiceService;
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
        userServiceService = ApiUtils.getUserService();
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
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                }

                else{
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.WelcomeMessage) +" "+ loggedInUser.getUserFullName(), Toast.LENGTH_LONG).show();

                    sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putInt("UserId",loggedInUser.getUserid().intValue());
                    editor.putInt("RoleId",loggedInUser.getRolesRolid());
                    editor.putString("UserFullName",loggedInUser.getUserFullName());
                    editor.putString("UserAcadmicID",loggedInUser.getUserAcadmicID());
                    editor.putString("QF",loggedInUser.getQf());
                    editor.putString("EXP",loggedInUser.getExp());
                    editor.putString("mobile",loggedInUser.getMobile());
                    editor.putString("sp",loggedInUser.getSp());
                    editor.putString("email",loggedInUser.getEmail());
                    editor.putString("empName",loggedInUser.getEmpName());
                    editor.commit();


               Move(loggedInUser);
                }
            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.DataInvalid), Toast.LENGTH_LONG).show();
            }
        }



        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }

    @VisibleForTesting
    public void Move(UserModel loggedInUser) {
        if(loggedInUser.getRolesRolid().intValue() == 2){
            Intent toAdminActivity = new Intent(this,AdminActivity.class);
            startActivity(toAdminActivity);

        }else{
        Intent toFormsActivit = new Intent(this,FormsActivity.class);
        startActivity(toFormsActivit);
        }
    }
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }
    @VisibleForTesting
    public void doLogin(String userName, String userPass) {
        if(userName.length() == 0  || userPass.length() == 0){
            Toast.makeText(this,getResources().getString(R.string.EmptyFieldErrorMsg),Toast.LENGTH_SHORT).show();

        }else {
            showProgress(true);
            AuthModel userModel = new AuthModel(userName, userPass);
            Call call = userServiceService.Authenticate(userModel);
            new UserLoginTask().execute(call);
        }
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginBtn){
            doLogin(username.getText().toString(),userpass.getText().toString());
        }
    }
    public void onBackPressed() {

    }
}
