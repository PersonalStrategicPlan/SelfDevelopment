package com.api_l.forms;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.Domains;
import com.api_l.forms.ListAdapters.DomainsAdapter;
import com.api_l.forms.ListAdapters.FormsAdapter;
import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.DomainsIntentModel;
import com.api_l.forms.Models.FormModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


public class DomainsActivity extends AppCompatActivity {
    private static DomainsAdapter adapter;
    private ListView listOfDomains;
    private ProgressBar progressBar;
   // private DomainsIntentModel domainModels = new DomainsIntentModel();
    private Domains domains;
    private ArrayList<DomainModel> dominsList = new ArrayList<DomainModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domains);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.domainsProgressBar);

        int formId = (int) getIntent().getIntExtra("formid",0);
        domains = ApiUtils.getDomains();
        loadAllFormDomain(formId);
       // Toast.makeText(getApplicationContext(),"Form iD"+formId,Toast.LENGTH_LONG).show();
       listOfDomains = (ListView) findViewById(R.id.domainsList);
      //  adapter = new DomainsAdapter(getApplicationContext(),domainModels.getDomainModels());
      // listOfDomains.setAdapter(adapter);

    }
    public void loadAllFormDomain(int formId){
    Call call = domains.GetAllDomains(formId);
        new LoadFormsTask().execute(call);
    }

public class LoadFormsTask extends AsyncTask<Call, Void, ArrayList<DomainModel>> {
    @Override
    protected ArrayList<DomainModel> doInBackground(Call... params) {
        Call<ArrayList<DomainModel>> c = params[0];
        ArrayList<DomainModel> forms = null;
        try {
            Response<ArrayList<DomainModel>> responseBody = c.execute();
            forms =responseBody.body();
            return  forms;
        } catch (IOException e) {
          //  Toast.makeText(FormsActivity.this, "Try again please!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return  forms;
    }

    @Override
    protected void onPostExecute(ArrayList<DomainModel>  forms) {
        showProgress(false);
        if(forms!=null) {
            listOfDomains = (ListView) findViewById(R.id.domainsList);
            adapter = new DomainsAdapter(DomainsActivity.this,forms);
            listOfDomains.setAdapter(adapter);
            // Toast.makeText(LoginActivity.this, "Hi " + loggedInUser.getUserFullName(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(DomainsActivity.this, "No Domains found!", Toast.LENGTH_LONG).show();
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
