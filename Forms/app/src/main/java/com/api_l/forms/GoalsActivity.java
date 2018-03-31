package com.api_l.forms;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.solver.Goal;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.Domains;
import com.api_l.forms.APIs.Goals;
import com.api_l.forms.APIs.Taqeems;
import com.api_l.forms.Dialogs.FormDialog;
import com.api_l.forms.Dialogs.NoticeDialogListener;
import com.api_l.forms.ListAdapters.DomainsAdapter;
import com.api_l.forms.ListAdapters.GoalsAdapter;
import com.api_l.forms.Models.AnswerModel;
import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.Models.TaqeemModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class GoalsActivity extends AppCompatActivity implements NoticeDialogListener,View.OnClickListener{
    private static GoalsAdapter adapter;
    private ListView listOfDomains;
    private ProgressBar progressBar;
    private Goals goals;
    private int domainId = 0;

    private ArrayList<GoalModel> goalList = new ArrayList<GoalModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.goalsProgressBar);

        domainId = (int) getIntent().getIntExtra("domainId",0);
        goals = ApiUtils.getGoals();
        loadAllGoals(domainId);
        // Toast.makeText(getApplicationContext(),"Form iD"+formId,Toast.LENGTH_LONG).show();
        listOfDomains = (ListView) findViewById(R.id.goalsList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.goalsFab);
        fab.setOnClickListener(this);
    }

    private void loadAllGoals(int domainId) {
        Call call = goals.GetAllDomainGoals(domainId);
        new LoadFormsTask().execute(call);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.goalsFab:
               // showProgress(true);
                Intent taqeemIntent = new Intent(this,DomainTaqeem.class);
                taqeemIntent.putExtra("domainId",domainId);
                startActivity(taqeemIntent);
                break;
        }
           }



    public class LoadFormsTask extends AsyncTask<Call, Void, ArrayList<GoalModel>> {
        @Override
        protected ArrayList<GoalModel> doInBackground(Call... params) {
            Call<ArrayList<GoalModel>> c = params[0];
            ArrayList<GoalModel> domainGoals = null;
            try {
                Response<ArrayList<GoalModel>> responseBody = c.execute();
                domainGoals =responseBody.body();
                return  domainGoals;
            } catch (IOException e) {
                //  Toast.makeText(FormsActivity.this, "Try again please!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return  domainGoals;
        }

        @Override
        protected void onPostExecute(ArrayList<GoalModel>  goals) {
            showProgress(false);
            if(goals!=null) {
                listOfDomains = (ListView) findViewById(R.id.goalsList);
                adapter = new GoalsAdapter(getApplicationContext(),goals,GoalsActivity.this);
                listOfDomains.setAdapter(adapter);
                // Toast.makeText(LoginActivity.this, "Hi " + loggedInUser.getUserFullName(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(GoalsActivity.this, "No Goals found!", Toast.LENGTH_LONG).show();
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


