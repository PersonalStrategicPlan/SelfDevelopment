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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.GoalServices;
import com.api_l.forms.ListAdapters.GoalsAdapter;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.Models.GoalsMetaDatum;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class GoalActivity extends AppCompatActivity implements View.OnClickListener{

    private GoalModel goalModel;
    private EditText goalbody,indecatorTxt,prj1txt,prj2txt,prj3txt;
    private TextView indecatorLbl,prj1lbl,prj2lbl,prj3lbl;
    private Button updateBtn,deleteBtn;
    private GoalServices goalServices;
    private SharedPreferences sharedPreferences;

    private ProgressBar progressBar;
    public GoalActivity(){}
    public GoalActivity(GoalModel goalModel){
        this.goalModel = goalModel;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        goalModel = (GoalModel) i.getSerializableExtra("goal");
        progressBar = (ProgressBar) findViewById(R.id.goalProgressBar);
        showProgress(false);
        goalServices = ApiUtils.getGoals();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        goalbody = (EditText) findViewById(R.id.goalBody);
        indecatorTxt = (EditText) findViewById(R.id.indecatorTxt);
        prj1txt = (EditText) findViewById(R.id.prj1Txt);
        prj2txt = (EditText) findViewById(R.id.prj2txt);
        prj3txt = (EditText) findViewById(R.id.prj3txt);

        prj1lbl = (TextView) findViewById(R.id.prj1lbl);
        prj2lbl = (TextView) findViewById(R.id.prj2lbl);
        prj3lbl = (TextView) findViewById(R.id.prj3lbl);
        indecatorLbl = (TextView) findViewById(R.id.indecatorLbl);

        deleteBtn = (Button) findViewById(R.id.deletebtn);
        updateBtn = (Button) findViewById(R.id.updatebtn);
        deleteBtn.setClickable(false);
        updateBtn.setClickable(false);
        if(goalModel !=null){
            goalbody.setText(goalModel.getGoal1());
            updateBtn.setClickable(true);
            deleteBtn.setClickable(true);
            updateBtn.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);
            if(goalModel.getFormsFormId() == null || goalModel.getFormsFormId().intValue() != 4){
                indecatorLbl.setVisibility(View.GONE);
                indecatorTxt.setVisibility(View.GONE);
                prj1lbl.setVisibility(View.GONE);
                prj1txt.setVisibility(View.GONE);
                prj2lbl.setVisibility(View.GONE);
                prj2txt.setVisibility(View.GONE);
                prj3lbl.setVisibility(View.GONE);
                prj3txt.setVisibility(View.GONE);
            }
            if(goalModel.getGoalsMetaData()!=null){
            for(GoalsMetaDatum item : goalModel.getGoalsMetaData()){
                if(item.getKey().equals("مؤشر الهدف")){

                    indecatorTxt.setText(item.getValue());
                }
                if(item.getKey().equals("المشروع الأول")){
                    prj1txt.setText(item.getValue());
                }
                else if(item.getKey().equals("المشروع الثاني")){
                    prj2txt.setText(item.getValue());
                }
                else if(item.getKey().equals("المشروع الثالث")){
                    prj3txt.setText(item.getValue());
                }
            }
            }
        }
        this.sharedPreferences  = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        int roleId = sharedPreferences.getInt("RoleId",1);
        if(roleId>1){
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }
    }
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }
    public final static boolean isValid(CharSequence target) {
        return (!TextUtils.isEmpty(target));
    }
    private void UpdateGoal(){
        showProgress(true);
        if(goalModel.getFormsFormId().intValue() != 4 && isValid(goalbody.getText().toString().trim())){
            goalModel.setGoal1(goalbody.getText().toString().trim());
            Call call = goalServices.UpdateGoal(goalModel);
            new GoalActivity.GoalProccessingTask().execute(call);
        }else{
            if(!checkProjectsEditTxt()){
                showProgress(false);
                Toast.makeText(this,"يجب إدخال مشروع واحد على الأقل",Toast.LENGTH_SHORT).show();
            }else {
                goalModel.setGoal1(goalbody.getText().toString().trim());
                ArrayList<GoalsMetaDatum> listOfMetaData = new ArrayList<GoalsMetaDatum>();
                GoalsMetaDatum project1 = new GoalsMetaDatum();
                GoalsMetaDatum project2 = new GoalsMetaDatum();
                GoalsMetaDatum project3 = new GoalsMetaDatum();
                GoalsMetaDatum Indecator = new GoalsMetaDatum();
                if(GetGoalMetaByKey("المشروع الأول") !=null){

                    GoalsMetaDatum item = GetGoalMetaByKey("المشروع الأول");
                    project1.setId(item.getId());

                }
                project1.setKey("المشروع الأول");
                project1.setValue(prj1txt.getText().toString());
                if(GetGoalMetaByKey("المشروع الثاني") !=null){

                    GoalsMetaDatum item = GetGoalMetaByKey("المشروع الثاني");
                    project2.setId(item.getId());

                }
                project2.setKey("المشروع الثاني");
                project2.setValue(prj2txt.getText().toString());
                if(GetGoalMetaByKey("المشروع الثالث") !=null){

                    GoalsMetaDatum item = GetGoalMetaByKey("المشروع الثالث");
                    project3.setId(item.getId());

                }
                project3.setKey("المشروع الثالث");
                project3.setValue(prj3txt.getText().toString());
                Indecator.setKey("مؤشر الهدف");
                Indecator.setValue(indecatorTxt.getText().toString());
                listOfMetaData.add(project1);
                listOfMetaData.add(project2);
                listOfMetaData.add(project3);
                listOfMetaData.add(Indecator);
                goalModel.setGoalsMetaData(listOfMetaData);
                Call call = goalServices.UpdateGoal(goalModel);
                new GoalActivity.GoalProccessingTask().execute(call);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.updatebtn:
                UpdateGoal();
                break;
            case R.id.deletebtn:
                showProgress(true);
                Call call = goalServices.DeleteGoal(goalModel.getGoalId());
                new GoalDeleteTask().execute(call);
                break;
        }
    }

    public class GoalProccessingTask extends AsyncTask<Call, Void, GoalModel> {
        @Override
        protected GoalModel doInBackground(Call... params) {
            Call<GoalModel> c = params[0];
            GoalModel domainGoals = null;
            try {
                Response<GoalModel> responseBody = c.execute();
                domainGoals =responseBody.body();
                return  domainGoals;
            } catch (IOException e) {
                //  Toast.makeText(FormsActivity.this, "Try again please!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return  domainGoals;
        }

        @Override
        protected void onPostExecute(GoalModel  goals) {
            showProgress(false);

              Move();

        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }

    }



    public class GoalDeleteTask extends AsyncTask<Call, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Call... params) {
            Call<Integer> c = params[0];
            try {
                Response<Integer> responseBody = c.execute();

                return  responseBody.isSuccessful();
            } catch (IOException e) {
                //  Toast.makeText(FormsActivity.this, "Try again please!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return  false;
        }

        @Override
        protected void onPostExecute(Boolean  isDone) {
            showProgress(false);

                Move();


        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }

    }
    public void Move(){
        Intent intent = new Intent(this,GoalsActivity.class);
        intent.putExtra("domainId",goalModel.getDomains_domainId());
        intent.putExtra("formId",goalModel.getFormsFormId());

        startActivity(intent);
    }

        private GoalsMetaDatum GetGoalMetaByKey(String key){
        GoalsMetaDatum item = null;
        if(goalModel!=null && goalModel.getGoalsMetaData()!=null){
        for(GoalsMetaDatum item_ : goalModel.getGoalsMetaData()){
            if(item_.getKey().equals(key)){
                item = item_;
            }
        }}
        return  item;
    }
    public boolean checkProjectsEditTxt() {
        if(prj1txt.getText().length() >0 || prj2txt.getText().length() >0 || prj3txt.getText().length() >0){

            return  true;
        }
        return  false;
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
