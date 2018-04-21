package com.api_l.forms;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.api_l.forms.Models.UserModel;

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
    private String title="";
    private int RoleId = 1;
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
        title = (String) getIntent().getStringExtra("title");
            setTitle(title);
        boolean isDomain = false;

        this.sharedPreferences  = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        listOfDomains = (ListView) findViewById(R.id.goalsList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.goalsFab);

        FloatingActionButton addGoal = (FloatingActionButton) findViewById(R.id.addGoal);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        RoleId = sharedPreferences.getInt("RoleId",0);

        if(RoleId ==2) {
            addGoal.hide();
        }

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
    public void GoalSelected(String value) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.goalsFab:

                Intent taqeemIntent = new Intent(this,DomainTaqeem.class);
                taqeemIntent.putExtra("domainId",domainId);
                taqeemIntent.putExtra("countOfGoals",countOfGoals);
                taqeemIntent.putExtra("formId",formId);
                taqeemIntent.putExtra("title",title);
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
                AddNewGoalDialog f = new AddNewGoalDialog(getUserId(),formId,domainId,goalList);
                f.show(fm, "flag01");
                }}
                break;
            case  R.id.colormeaning:
                ShowColorMeaningDialog();
                break;
        }
           }

    private void ShowColorMeaningDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.color_meaning_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("معاني ألوان التقييم");
        dialogBuilder.setNegativeButton("إخفاء", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
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
                e.printStackTrace();
            }

            return  domainGoals;
        }

        @Override
        protected void onPostExecute(ArrayList<GoalModel>  goals) {
            showProgress(false);
            if(goals!=null) {
                countOfGoals = goals.size();
                goalList = goals;
                listOfDomains = (ListView) findViewById(R.id.goalsList);
                adapter = new GoalsAdapter(getApplicationContext(),goals,GoalsActivity.this,formId,title);
                listOfDomains.setAdapter(adapter);

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
            case R.id.logout:
                Logout();
                return true;
            case  R.id.colormeaning:
                ShowColorMeaningDialog();
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


