package com.api_l.forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.DomainServices;
import com.api_l.forms.ListAdapters.DomainsAdapter;
import com.api_l.forms.Models.DomainModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


public class DomainsActivity extends AppCompatActivity implements View.OnClickListener{
    private static DomainsAdapter adapter;
    private ListView listOfDomains;
    private ProgressBar progressBar;
    private int formId =0;
    private SharedPreferences sharedPreferences;

    // private DomainsIntentModel domainModels = new DomainsIntentModel();
    private DomainServices domainServices;
    private ArrayList<DomainModel> dominsList = new ArrayList<DomainModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domains);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.domainsProgressBar);

        formId = (int) getIntent().getIntExtra("formId",0);
        domainServices = ApiUtils.getDomains();
        loadAllFormDomain(formId);
       // Toast.makeText(getApplicationContext(),"Form iD"+formId,Toast.LENGTH_LONG).show();
       listOfDomains = (ListView) findViewById(R.id.domainsList);
//        listOfDomains.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.DomainsTaqeemFab);
        if(formId == 4 || formId == 3){

            fab.setVisibility(View.GONE);
        }


        fab.setOnClickListener(this);
      //  goalServices = ApiUtils.getGoals();
       // loadAllGoals(formId,domainId,isDomain);


        //  adapter = new DomainsAdapter(getApplicationContext(),domainModels.getDomainModels());
      // listOfDomains.setAdapter(adapter);

    }
    public void loadAllFormDomain(int formId){
    Call call = domainServices.GetAllDomains(formId);
        new LoadFormsTask().execute(call);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case  R.id.DomainsTaqeemFab:
        // showProgress(true);
        Intent taqeemIntent = new Intent(this,DomainTaqeem.class);
        taqeemIntent.putExtra("formId",formId);
        taqeemIntent.putExtra("isFormTaqeem",true);
        startActivity(taqeemIntent);
        break;
        }
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
            Toast.makeText(DomainsActivity.this, "No DomainServices found!", Toast.LENGTH_LONG).show();
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
