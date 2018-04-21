package com.api_l.forms.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.api_l.forms.APIs.AnswerServices;
import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.DomainServices;
import com.api_l.forms.APIs.FormServices;
import com.api_l.forms.APIs.GoalServices;
import com.api_l.forms.DomainsActivity;
import com.api_l.forms.FormsActivity;
import com.api_l.forms.GoalsActivity;
import com.api_l.forms.ListAdapters.DomainsAdapter;
import com.api_l.forms.ListAdapters.FormsAdapter;
import com.api_l.forms.ListAdapters.GoalsAdapter;
import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.FormModel;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.R;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


public class ChoseInsertedGoalDialog extends DialogFragment implements NoticeDialogListener, View.OnClickListener  {

    private FormServices formServicesApi;
    private DomainServices domainServices;
    private GoalServices goalServices;
    private ProgressBar progressBar;
    private boolean isFiveLevels = true;
    private Spinner domainsSpiner,goalsSpiner;
    private ArrayList<DomainModel> domainModelList = new ArrayList<DomainModel>();
    private  ArrayList<GoalModel> goalModelList = new ArrayList<GoalModel>();
    private ArrayList<String> domainsTitles = new ArrayList<String>();
    private ArrayList<String> goalsTitles = new ArrayList<String>();
   private ArrayAdapter<String> adapter ;
    private ArrayAdapter<String> goaladapter ;
    private int goalId;
    private SharedPreferences sharedPreferences;
    private Button confirmBtn;

    private NoticeDialogListener listener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
        View promptView = layoutInflater.inflate(R.layout.chose_goal_dialog, null);
        progressBar = (ProgressBar)promptView.findViewById(R.id.chosenInsertedGoalProgress);
        showProgress(false);
        domainsSpiner = (Spinner)promptView.findViewById(R.id.spinner);
        goalsSpiner = (Spinner) promptView.findViewById(R.id.spinner2);
        confirmBtn = (Button) promptView.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);
        domainsSpiner.setAdapter(adapter);
        domainsSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                UpdateGoalSpiner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                goalsTitles = new ArrayList<String>();
                goaladapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, goalsTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                domainsSpiner.setAdapter(adapter);

            }

        });
        domainServices = ApiUtils.getDomains();
        goalServices =ApiUtils.getGoals();
        loadAllFormDomain(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("").setTitle(getResources().getString(R.string.TitleChoseGoal))
                .setView(promptView)
                .setNegativeButton(getResources().getString(R.string.BACK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    private void UpdateGoalSpiner(int position) {
        if(domainModelList.size()>0){
        DomainModel domain = domainModelList.get(position);
        loadAllGoals(1,domain.getDomainID().intValue(),true);
        }
    }

    public void loadAllGoals(Integer formId,Integer domainId,boolean isDomain) {

        showProgress(true);
        goalsTitles = new ArrayList<String>();
        goaladapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, goalsTitles);
        goaladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsSpiner.setAdapter(goaladapter);
        Call call = goalServices.GetAllGoals(formId, domainId, getUserId(), isDomain);
        new LoadGoals().execute(call);
    }

    private int getUserId()
    {

        int u = 0;
        this.sharedPreferences  = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
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

    public NoticeDialogListener getListener() {
        return listener;
    }

    public void setListener(NoticeDialogListener listener) {
        this.listener = listener;
    }

    public class LoadGoals extends AsyncTask<Call, Void, ArrayList<GoalModel>> {
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
                GetGoalsTitles(goals);
                FillGoalsSpinner(goalsTitles);
            }else {
                Toast.makeText(getActivity(), "No GoalServices found!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }

    private void GetGoalsTitles(ArrayList<GoalModel> goals) {
        for(GoalModel d : goals){
            goalsTitles.add(d.getGoal1());
        }
    }
    private void FillGoalsSpinner(ArrayList<String> goalsTitles) {
        goaladapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, goalsTitles);
        goaladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsSpiner.setAdapter(goaladapter);

    }
    public void loadAllFormDomain(int formId)
    {

        showProgress(true);
        Call call = domainServices.GetAllDomains(formId);
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

                e.printStackTrace();
            }

            return  forms;
        }

        @Override
        protected void onPostExecute(ArrayList<DomainModel>  domains) {
            showProgress(false);
            if(domains!=null) {

                domainModelList = domains;
                GetDomainsTitles(domains);
                FillSpinner(domainsTitles);

            }else {
                Toast.makeText(getActivity(), "No DomainServices found!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }

    private void GetDomainsTitles(ArrayList<DomainModel> domains){
       for(DomainModel d : domains){
           domainsTitles.add(d.getDomainTitle());
       }


    }
    private void FillSpinner(ArrayList<String> domainsTitles) {
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, domainsTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domainsSpiner.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
       listener.GoalSelected(goalsSpiner.getSelectedItem().toString());
        dismiss();

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void GoalSelected(String value) {


    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }
}
