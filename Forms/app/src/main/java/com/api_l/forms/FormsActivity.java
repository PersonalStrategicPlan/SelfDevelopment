package com.api_l.forms;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.Forms;
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
    private Forms formsApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.formsProgressBar);
        formsApi = ApiUtils.getForms();
        loadAllForms();


    }

    private void loadAllForms() {

        Call call = formsApi.GetAllForms();
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

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }

}
