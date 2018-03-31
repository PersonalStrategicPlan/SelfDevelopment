package com.api_l.forms;

import android.app.DialogFragment;
import android.app.FragmentManager;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.GoalServices;
import com.api_l.forms.Dialogs.AddNewGoalDialog;
import com.api_l.forms.Dialogs.NoticeDialogListener;
import com.api_l.forms.ListAdapters.GoalsAdapter;
import com.api_l.forms.Models.GoalModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class GoalsActivity extends AppCompatActivity implements NoticeDialogListener,View.OnClickListener {
    private static GoalsAdapter adapter;
    private ListView listOfDomains;
    private ProgressBar progressBar;
    private GoalServices goalServices;
    private int domainId = 0;
    private int formId = 0;
    private  int countOfGoals =0;
    private boolean isPublic = false;
    private SharedPreferences sharedPreferences;
    private ArrayList<GoalModel> goalList = new ArrayList<GoalModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.goalsProgressBar);

        domainId = (int) getIntent().getIntExtra("domainId",0);
        formId = (int) getIntent().getIntExtra("formId",0);
        boolean isDomain = false;


       // domainId = (formId != 0 ? domainId=formId:domainId);
        // Toast.makeText(getApplicationContext(),"Form iD"+formId,Toast.LENGTH_LONG).show();
        listOfDomains = (ListView) findViewById(R.id.goalsList);
        //registerForContextMenu(listOfDomains);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.goalsFab);

        FloatingActionButton addGoal = (FloatingActionButton) findViewById(R.id.addGoal);
        if(formId != 4 && formId != 3){
            isPublic = true;
            isDomain = true;
            addGoal.setVisibility(View.GONE);
        }
        if(formId == 4){
            fab.setVisibility(View.GONE);
        }
        addGoal.setOnClickListener(this);
        fab.setOnClickListener(this);
        goalServices = ApiUtils.getGoals();
        loadAllGoals(formId,domainId,isDomain);


    }

    private void loadAllGoals(Integer formId,Integer domainId,boolean isDomain)
    {


        if(domainId==0){
            Call call = goalServices.GetAllGoals(formId,null,getUserId(),isDomain);
            new LoadFormsTask().execute(call);
        }else {
            Call call = goalServices.GetAllGoals(formId, domainId, getUserId(), isDomain);
            new LoadFormsTask().execute(call);
        }
    }
    private int getUserId()
    {

        int u = 0;
        this.sharedPreferences  = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        int userid = sharedPreferences.getInt("UserId",0);
        int roleId = sharedPreferences.getInt("RoleId",1);
        int target = sharedPreferences.getInt("Target",0);
        if(roleId == 2){
            u = target;
        }else {
            u = userid;
        }
        return u;
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
                taqeemIntent.putExtra("countOfGoals",countOfGoals);
                taqeemIntent.putExtra("formId",formId);
                startActivity(taqeemIntent);
                break;
            case R.id.addGoal:
                int roleId = sharedPreferences.getInt("RoleId",1);
                if(roleId !=2){
                if((formId == 3 && countOfGoals >10) ||(formId == 4 && countOfGoals >9)) {
                    Toast.makeText(this,"تجاوزت الحد المسموح به للإضافة",Toast.LENGTH_LONG).show();
                }
                else{
                FragmentManager fm = getFragmentManager();
                AddNewGoalDialog f = new AddNewGoalDialog(getUserId(),formId,domainId);
               // f.setGoalId(model.getGoalId());
                f.show(fm, "flag01");
                }}
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
                countOfGoals = goals.size();
                listOfDomains = (ListView) findViewById(R.id.goalsList);
                adapter = new GoalsAdapter(getApplicationContext(),goals,GoalsActivity.this,formId);
                listOfDomains.setAdapter(adapter);
                // Toast.makeText(LoginActivity.this, "Hi " + loggedInUser.getUserFullName(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(GoalsActivity.this, "No GoalServices found!", Toast.LENGTH_LONG).show();
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


