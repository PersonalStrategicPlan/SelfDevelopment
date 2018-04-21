package com.api_l.forms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
    // variables
    private UserService userServiceService;
    private ProgressBar progressBar;
    private ListView userListView;
    private Button searchBtn;
    private EditText searchTxt;
    private SharedPreferences sharedPreferences;
    private int RoleId = 1;

    private ArrayList<UserModel> userList = new ArrayList<UserModel>();
    private ArrayAdapter<UserModel> adapter;

    // make design to admin page by indicate each button to specific action
    @Override
     protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // indicate each button by given id to each one
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchTxt = (EditText)findViewById(R.id.searchTxt);
        searchBtn.setOnClickListener(this);
        userListView = (ListView) findViewById(R.id.UsersListView);
        userListView.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.adminProgressBar);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        RoleId = sharedPreferences.getInt("RoleId",0);
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
                // check case if the admin press search without enter id for specific user
                if(searchTxt.getText().length()==0){
                    Toast.makeText(this,getResources().getString(R.string.AdminSearchPlaceHolder), Toast.LENGTH_SHORT).show();
                }else{

                UserModel user = null;
                user = searchInUserList();
                    // will move to userInfo page if admin enter valid academic's ID or name
                if(user!=null){
                    MoveToUserInfo(user);
                    // if admin enter invalid academic's ID or name
                }else{
                    Toast.makeText(this,getResources().getString(R.string.UserNotFoundInSearchList),Toast.LENGTH_LONG).show();
                }
                }
                break;
        }
    }
 // this method for move userInfo page
    private void MoveToUserInfo(UserModel user) {
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt("Target",user.getUserid().intValue());
        editor.commit();
        Intent intent = new Intent(this,UserInfoActivity.class);
        intent.putExtra("userModel",user);
        startActivity(intent);
    }

    private UserModel searchInUserList() {
        for (UserModel user : userList){
            if(user.getUserAcadmicID().equals(searchTxt.getText().toString().trim()) || user.getUserFullName().contains(searchTxt.getText().toString().trim())){
                return user;
            }
        }
        return  null;
    }
// used AsyncTask to perform connection process that connects to API endpoint and get the JSON result
    public class LoadAllUsersTask extends AsyncTask<Call, Void, ArrayList<UserModel>> {
        @Override
        protected ArrayList<UserModel> doInBackground(Call... params) {
            Call<ArrayList<UserModel>> c = params[0];
            ArrayList<UserModel> users = null;


            try {
                // response from database
                Response<ArrayList<UserModel>> responseBody = c.execute();
                users =responseBody.body();
                return  users;
            } catch (IOException e) {

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
                userListView.setVisibility(View.GONE);

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
        if(RoleId >1){
            inflater.inflate(R.menu.admin_options,menu);
        }else{
        inflater.inflate(R.menu.user_options, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile:
                MoveToProfile();
                return true;
            case  R.id.colormeaning:
                ShowColorMeaningDialog();
                return true;
            case R.id.logout:
                Logout();
                return true;
            case R.id.forms:
                MoveToForms();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void MoveToForms() {
        Intent toFormsActivit = new Intent(this,FormsActivity.class);
        startActivity(toFormsActivit);
    }

    private void ShowColorMeaningDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.color_meaning_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(getResources().getString(R.string.EvaluationRanksMeaning));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.HIDE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void Logout() {
        this.sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        this.sharedPreferences.edit().clear().commit();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);

    }

    private void MoveToProfile() {
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("UserId",0);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt("Target",userId);
        editor.commit();
        UserModel model = new UserModel();
        model.setUserid(sharedPreferences.getInt("UserId",0));
        model.setUserFullName(sharedPreferences.getString("UserFullName",""));
        model.setUserAcadmicID(sharedPreferences.getString("UserAcadmicID",""));
        model.setEmail(sharedPreferences.getString("email",""));
        model.setQf(sharedPreferences.getString("QF",""));
        model.setuserExp(sharedPreferences.getString("EXP",""));
        model.setEmpName(sharedPreferences.getString("empName",""));
        model.setSp(sharedPreferences.getString("sp",""));
        model.setMobile(sharedPreferences.getString("mobile",""));
        Intent  i = new Intent(this,UserInfoActivity.class);
        i.putExtra("userModel",model);
        startActivity(i);
    }

}
