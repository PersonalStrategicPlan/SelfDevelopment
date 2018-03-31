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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.TaqeemServices;
import com.api_l.forms.Models.TaqeemModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_taqeem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        domainId = (int) getIntent().getIntExtra("domainId",0); //
        formId = (int) getIntent().getIntExtra("formId",0); //
        countOfgoals = (int) getIntent().getIntExtra("countOfGoals",0); //
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
                taqeemTitle.setText("التقييم على مستوى التمرين");
                Call call = taqeemServices.GetFormTaqeem(userid, formId);
                new LoadTaqeem().execute(call);
            }else{
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
                //  Toast.makeText(FormsActivity.this, "Try again please!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(DomainTaqeem.this, "لا يوجد تقييم مسجل لحد الآن!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }
    private void UpdateView(TaqeemModel taqeemModel) {
        if(taqeemModel!=null){
            t1.setText("عدد التقييمات : "+taqeemModel.getSoHappyCount().toString() + " - "+"النسبة : "+ new DecimalFormat("##.##").format((((double)taqeemModel.getSoHappyCount()/15)*100))+" %");
            t2.setText("عدد التقييمات : "+taqeemModel.getHappyCount().toString() + " - "+"النسبة : "+ new DecimalFormat("##.##").format((((double)taqeemModel.getHappyCount()/15)*100))+" %");
            t3.setText("عدد التقييمات : "+taqeemModel.getAvaCount().toString()+ " - "+"النسبة : "+ new DecimalFormat("##.##").format((((double)taqeemModel.getAvaCount()/15)*100))+" %");
            t4.setText("عدد التقييمات : "+taqeemModel.getNotHappyCount().toString()+ " - "+"النسبة : "+ new DecimalFormat("##.##").format((((double)taqeemModel.getNotHappyCount()/15)*100))+" %");
            t5.setText("عدد التقييمات : "+taqeemModel.getSadCount().toString()+ " - "+"النسبة :"+ new DecimalFormat("##.##").format((((double)taqeemModel.getSadCount()/15)*100))+" %");
            //percentagetxt.setText(taqeemModel.getPercentage().toString()+" %");
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
