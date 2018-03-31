package com.api_l.forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.UserService;
import com.api_l.forms.ListAdapters.GoalsAdapter;
import com.api_l.forms.ListAdapters.UsersAdapter;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.Models.UserModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private UserService userServiceService;
    private ProgressBar progressBar;
    private ListView userListView;
    private Button searchBtn;
    private EditText searchTxt;
    private SharedPreferences sharedPreferences;

    private ArrayList<UserModel> userList = new ArrayList<UserModel>();
    private ArrayAdapter<UserModel> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       searchBtn = (Button)findViewById(R.id.searchBtn);
        searchTxt = (EditText)findViewById(R.id.searchTxt);
        searchBtn.setOnClickListener(this);
        userListView = (ListView) findViewById(R.id.UsersListView);
        progressBar = (ProgressBar) findViewById(R.id.adminProgressBar);
        showProgress(false);
        userServiceService = ApiUtils.getUserService();
        LoadAllUsers();
    }

    private void LoadAllUsers() {
        showProgress(true);
        Call call = userServiceService.GetUsers();
        new AdminActivity.LoadAllUsersTask().execute(call);

    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.searchBtn:
                UserModel user = null;
                user = searchInUserList();
                if(user!=null){
                Intent intent = new Intent(this,UserInfoActivity.class);
                intent.putExtra("userModel",user);
                startActivity(intent);
                }else{
                    Toast.makeText(this,"لا يوجد عضو له نفس الرقم الاكاديمي المدخل!",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private UserModel searchInUserList() {
        for (UserModel user : userList){
            if(user.getUserAcadmicID().equals(searchTxt.getText().toString().trim())){
                return user;
            }
        }
        return  null;
    }

    public class LoadAllUsersTask extends AsyncTask<Call, Void, ArrayList<UserModel>> {
        @Override
        protected ArrayList<UserModel> doInBackground(Call... params) {
            Call<ArrayList<UserModel>> c = params[0];
            ArrayList<UserModel> users = null;
            try {
                Response<ArrayList<UserModel>> responseBody = c.execute();
                users =responseBody.body();
                return  users;
            } catch (IOException e) {
                //  Toast.makeText(FormsActivity.this, "Try again please!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return  users;
        }

        @Override
        protected void onPostExecute(ArrayList<UserModel>  users) {
            showProgress(false);
            if(users!=null) {
                userList = users;
                adapter = new UsersAdapter(getApplicationContext(),users,AdminActivity.this);
                userListView.setAdapter(adapter);
                // Toast.makeText(LoginActivity.this, "Hi " + loggedInUser.getUserFullName(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(AdminActivity.this, "No Users found!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                MoveToProfile();
                return true;
            case R.id.logout:
                Logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Logout() {
        this.sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        this.sharedPreferences.edit().clear().commit();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);

    }

    private void MoveToProfile() {
        Intent  i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

}
