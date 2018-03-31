package com.api_l.forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.FormServices;
import com.api_l.forms.ListAdapters.FormsAdapter;

import com.api_l.forms.Models.FormModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class FormsActivity extends AppCompatActivity {
    private static FormsAdapter adapter;
    private ListView listOfForms;
    private ProgressBar progressBar;
    private FormServices formServicesApi;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.formsProgressBar);
        formServicesApi = ApiUtils.getForms();
        loadAllForms();


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

    private void loadAllForms() {

        Call call = formServicesApi.GetAllForms();
        new LoadFormsTask().execute(call);
    }

    public class LoadFormsTask extends AsyncTask<Call, Void, ArrayList<FormModel>> {
        @Override
        protected ArrayList<FormModel> doInBackground(Call... params) {
            Call<ArrayList<FormModel>> c = params[0];
            ArrayList<FormModel> forms = null;
            try {
                Response<ArrayList<FormModel>> responseBody = c.execute();
                forms =responseBody.body();
                return  forms;
            } catch (IOException e) {
                Toast.makeText(FormsActivity.this, "Try again please!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return  forms;
        }

        @Override
        protected void onPostExecute(ArrayList<FormModel>  forms) {
            showProgress(false);
            if(forms!=null) {
                listOfForms = (ListView) findViewById(R.id.formsList);
                adapter = new FormsAdapter(FormsActivity.this,forms);
                listOfForms.setAdapter(adapter);
               // Toast.makeText(LoginActivity.this, "Hi " + loggedInUser.getUserFullName(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(FormsActivity.this, "Not Found", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }

    @Override
    public void onBackPressed() {
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        int roleId = sharedPreferences.getInt("RoleId",1);
        if(roleId>1){

           super.onBackPressed();
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }

}
