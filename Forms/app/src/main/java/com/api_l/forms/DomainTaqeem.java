package com.api_l.forms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.TaqeemServices;
import com.api_l.forms.Models.TaqeemModel;
import com.api_l.forms.Models.UserModel;

import java.io.IOException;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Response;

public class DomainTaqeem extends AppCompatActivity {
    private TaqeemServices taqeemServices;
    private int domainId = 0 , countOfgoals = 0, formId=0;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private TextView t1,t2,t3,t4,t5,taqeemTitle,percentagetxt;
    private ImageView soHappy,happy,avarage,notHappy,sad;
    private boolean isFormTaqeem = false;
    private String title;

    private int RoleId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_taqeem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        domainId = (int) getIntent().getIntExtra("domainId",0);
        formId = (int) getIntent().getIntExtra("formId",0);
        countOfgoals = (int) getIntent().getIntExtra("countOfGoals",0);
        title = (String) getIntent().getStringExtra("title");
        isFormTaqeem =(boolean) getIntent().getBooleanExtra("isFormTaqeem",false);
        progressBar = (ProgressBar) findViewById(R.id.taqeemProgressBar);
        t1 = (TextView) findViewById(R.id.soHappyCount);
        t2 = (TextView) findViewById(R.id.happyCount);
        t3 = (TextView) findViewById(R.id.AvaCount);
        t4 = (TextView) findViewById(R.id.notHappayCount);
        t5 = (TextView) findViewById(R.id.SadCount);
        percentagetxt = (TextView) findViewById(R.id.percentagetxt);
        soHappy = (ImageView) findViewById(R.id.imageView1);
        happy = (ImageView) findViewById(R.id.imageView2);
        avarage = (ImageView) findViewById(R.id.imageView3);
        notHappy = (ImageView) findViewById(R.id.imageView4);
        sad = (ImageView)findViewById(R.id.imageView5);

       taqeemTitle = (TextView) findViewById(R.id.taqeemTitle);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        RoleId = sharedPreferences.getInt("RoleId",0);


        showProgress(true);
        ShowTaqeem();

    }
    private  void ShowTaqeem()
    {
        if(formId == 3){
            showProgress(false);

            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            t3.setVisibility(View.GONE);
            t4.setVisibility(View.GONE);
            t5.setVisibility(View.GONE);
            soHappy.setVisibility(View.GONE);
            happy.setVisibility(View.GONE);
            avarage.setVisibility(View.GONE);
            notHappy.setVisibility(View.GONE);
            sad.setVisibility(View.GONE);
            double percentage =  (countOfgoals/10.0)*100;
            percentagetxt.setText(String.valueOf(percentage)+" %\n"+ String.valueOf(countOfgoals)+" of 10 ");
        }else {

            int userid = getUserId();
            taqeemServices = ApiUtils.Taqeem();
            if(isFormTaqeem){
                taqeemTitle.setText(getResources().getString(R.string.EvaluationFormsTitle) + " :"+title);
                Call call = taqeemServices.GetFormTaqeem(userid, formId);
                new LoadTaqeem().execute(call);
            }else{
                taqeemTitle.append(": "+title);
            Call call = taqeemServices.GetDomainTaqeem(userid, domainId);
            new LoadTaqeem().execute(call);
            }
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
    public class LoadTaqeem extends AsyncTask<Call, Void, TaqeemModel> {
        @Override
        protected TaqeemModel doInBackground(Call... params) {
            Call<TaqeemModel> c = params[0];
            TaqeemModel taqeemModel = new TaqeemModel();
            try {
                Response<TaqeemModel> responseBody = c.execute();
                taqeemModel =responseBody.body();
                return  taqeemModel;
            } catch (IOException e) {

                e.printStackTrace();
            }

            return  taqeemModel;
        }

        @Override
        protected void onPostExecute(TaqeemModel  taqeemModel) {
            showProgress(false);
            if(taqeemModel.getAvaCount() != null){
                UpdateView(taqeemModel);
            }else {
                Toast.makeText(DomainTaqeem.this, getResources().getString(R.string.NoEvaluationFoundYet), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }
    private void UpdateView(TaqeemModel taqeemModel) {
        if(taqeemModel!=null && isFormTaqeem == false){
            t1.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getSoHappyCount().toString() + " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getSoHappyCount()/15)*100))+" %");
            t2.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getHappyCount().toString() + " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getHappyCount()/15)*100))+" %");
            t3.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getAvaCount().toString()+ " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getAvaCount()/15)*100))+" %");
            t4.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getNotHappyCount().toString()+ " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getNotHappyCount()/15)*100))+" %");
            t5.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getSadCount().toString()+ " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getSadCount()/15)*100))+" %");

        }
       else{
            t1.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getSoHappyCount().toString() + " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getSoHappyCount()/90)*100))+" %");
            t2.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getHappyCount().toString() + " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getHappyCount()/90)*100))+" %");
            t3.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getAvaCount().toString()+ " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getAvaCount()/90)*100))+" %");
            t4.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getNotHappyCount().toString()+ " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getNotHappyCount()/90)*100))+" %");
            t5.setText(getResources().getString(R.string.EvaluationCount)+taqeemModel.getSadCount().toString()+ " - "+getResources().getString(R.string.Percentage)+ new DecimalFormat("##.##").format((((double)taqeemModel.getSadCount()/90)*100))+" %");

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
            case  R.id.colormeaning:
                ShowColorMeaningDialog();
                return true;
            case R.id.logout:
                Logout();
                return true;
            case  R.id.forms:
                MoveToForms();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void MoveToForms() {
        Intent toFormsActivit = new Intent(this,FormsActivity.class);
        startActivity(toFormsActivit);
    }

    private void ShowColorMeaningDialog() {
        String DialogTitle = getResources().getString(R.string.EvaluationRanksMeaning);
        String DialogHide = getResources().getString(R.string.HIDE);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.color_meaning_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(DialogTitle);
        dialogBuilder.setNegativeButton(DialogHide, new DialogInterface.OnClickListener() {
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
