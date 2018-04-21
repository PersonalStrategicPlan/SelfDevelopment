package com.api_l.forms;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.DomainServices;
import com.api_l.forms.ListAdapters.DomainsAdapter;
import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.UserModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


public class DomainsActivity extends AppCompatActivity implements View.OnClickListener{
    private static DomainsAdapter adapter;
    private ListView listOfDomains;
    private ProgressBar progressBar;
    private int formId =0;
    private String title ="";
    private SharedPreferences sharedPreferences;
    private int RoleId = 1;


    private DomainServices domainServices;
    private ArrayList<DomainModel> dominsList = new ArrayList<DomainModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domains);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        RoleId = sharedPreferences.getInt("RoleId",0);

        progressBar = (ProgressBar) findViewById(R.id.domainsProgressBar);

        formId = (int) getIntent().getIntExtra("formId",0);
        ChangeActivityTitle();
        domainServices = ApiUtils.getDomains();
        loadAllFormDomain(formId);

       listOfDomains = (ListView) findViewById(R.id.domainsList);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.DomainsTaqeemFab);
        if(formId == 4 || formId == 3){

            fab.setVisibility(View.GONE);
        }


        fab.setOnClickListener(this);


    }

    private void ChangeActivityTitle() {
        switch (formId){
            case 1:
                title = "مجالات النموذج الأول";
                this.setTitle(title);

                break;
            case 2:
                title ="مجالات النموذج الثاني";
                this.setTitle(title);
                break;
            case 3:
                title = "مجالات النموذج الثالث";
                this.setTitle(title);
                break;
            case 4:
                title  = "مجالات النموذج الرابع";
                this.setTitle(title);
                break;
        }
    }

    public void loadAllFormDomain(int formId){
    Call call = domainServices.GetAllDomains(formId);
        new LoadFormsTask().execute(call);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case  R.id.DomainsTaqeemFab:

        Intent taqeemIntent = new Intent(this,DomainTaqeem.class);
        taqeemIntent.putExtra("formId",formId);
        taqeemIntent.putExtra("isFormTaqeem",true);
        taqeemIntent.putExtra("title",title);
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
            case R.id.forms:
                MoveToForms();
                return true;
            case  R.id.colormeaning:
                ShowColorMeaningDialog();
                return true;
            case R.id.logout:
                Logout();
                return true;
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
