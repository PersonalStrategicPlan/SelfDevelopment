package com.api_l.forms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.Taqeems;
import com.api_l.forms.Models.TaqeemModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class DomainTaqeem extends AppCompatActivity {
    private Taqeems taqeems;
    private int domainId = 0;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private TextView t1,t2,t3,t4,t5,percentagetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_taqeem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        domainId = (int) getIntent().getIntExtra("domainId",0);
        progressBar = (ProgressBar) findViewById(R.id.taqeemProgressBar);
        t1 = (TextView) findViewById(R.id.soHappyCount);
        t2 = (TextView) findViewById(R.id.happyCount);
        t3 = (TextView) findViewById(R.id.AvaCount);
        t4 = (TextView) findViewById(R.id.notHappayCount);
        t5 = (TextView) findViewById(R.id.SadCount);
        percentagetxt = (TextView) findViewById(R.id.PercentageTxt);

        showProgress(true);
        ShowTaqeem();

    }
    private  void ShowTaqeem(){
        this.sharedPreferences  = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        int userid = sharedPreferences.getInt("UserId",0);
        taqeems = ApiUtils.Taqeem();
        Call call = taqeems.GetDomainTaqeem(userid,domainId);
        new LoadTaqeem().execute(call);
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
            t1.setText(taqeemModel.getSoHappyCount().toString());
            t2.setText(taqeemModel.getHappyCount().toString());
            t3.setText(taqeemModel.getAvaCount().toString());
            t4.setText(taqeemModel.getNotHappyCount().toString());
            t5.setText(taqeemModel.getSadCount().toString());
            percentagetxt.setText(taqeemModel.getPercentage().toString()+" %");
        }
    }
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }



}
