package com.api_l.forms.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.GoalServices;
import com.api_l.forms.GoalsActivity;
import com.api_l.forms.Helper.Constants;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.Models.GoalsMetaDatum;
import com.api_l.forms.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;



public class AddNewGoalDialog extends DialogFragment implements NoticeDialogListener, View.OnClickListener {
    private ProgressBar progressBar;
    private int goalId;
    private Button saveBtn,choseBtn;
    private GoalServices goalServices;
    private EditText goalBody,goalIndecator,project1,project2,project3;
    private Integer formId,domainId;
    private TextView writeGoalTxt;
    private int userId = 0;
    private SharedPreferences sharedPreferences;
    private ArrayList<GoalModel> insertedGoals = new ArrayList<>();
    private TextView txtIndecator ,projectslbl;
    public AddNewGoalDialog(int userId, Integer formId, Integer domainId, ArrayList<GoalModel> goals) {
        this.userId = userId;
        this.domainId = domainId;
        this.formId =formId;
        this.insertedGoals = goals;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
        View promptView = layoutInflater.inflate(R.layout.add_new_goal_layout, null);
        goalServices = ApiUtils.getGoals();
        progressBar = (ProgressBar) promptView.findViewById(R.id.addnewgoalProgressbar);
        goalBody = (EditText) promptView.findViewById(R.id.goalbodytxt);
        goalBody.setEnabled(false);
        goalIndecator =(EditText) promptView.findViewById(R.id.goalindecator);
        txtIndecator = (TextView) promptView.findViewById(R.id.textView3);
        projectslbl = (TextView) promptView.findViewById(R.id.projectsLbl);
        project1 = (EditText) promptView.findViewById(R.id.project1);
        project2 = (EditText) promptView.findViewById(R.id.project2);
        project3 = (EditText) promptView.findViewById(R.id.project3);
        writeGoalTxt = (TextView) promptView.findViewById(R.id.writeGoallbl);
        choseBtn = (Button) promptView.findViewById(R.id.choseGoalBtn);
        choseBtn.setVisibility(View.GONE);
        goalBody.setEnabled(true);

        writeGoalTxt.setText("فضلاً اكتب الهدف");
        if(domainId == 22){
            choseBtn.setVisibility(View.VISIBLE);
            writeGoalTxt.setText("فضلاً اختر الهدف");
            goalBody.setEnabled(false);

        }
        choseBtn.setOnClickListener(this);
        if(formId == 3){
            goalIndecator.setVisibility(View.GONE);
            txtIndecator.setVisibility(View.GONE);
            projectslbl.setVisibility(View.GONE);
            project1.setVisibility(View.GONE);
            project2.setVisibility(View.GONE);
            project3.setVisibility(View.GONE);
            choseBtn.setVisibility(View.GONE);

        }

        showProgress(false);
        saveBtn = (Button) promptView.findViewById(R.id.savebtn);
        saveBtn.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("إضافة هدف جديد").setTitle("الأهداف")
                .setView(promptView)
                .setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        onDialogPositiveClick(AddNewGoalDialog.this);
                    }
                });
        return builder.create();
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
        goalBody.setText(value);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.savebtn:
                addGoal();
                break;
            case R.id.choseGoalBtn:
                Log.d("clicked","Here");
                FragmentManager fm = getFragmentManager();
                ChoseInsertedGoalDialog f = new ChoseInsertedGoalDialog();
                f.setListener(this);

                f.show(fm, "flag11");
                break;
        }
    }

    private  void addGoal(){

        if(!checkProjectsEditTxt(formId) && formId == 4){
            showProgress(false);
          }else {
            if(checkProjectsEditTxt(formId)){
            GoalModel goal = new GoalModel();
            goal.setUsers_userid(userId);

            goal.setDomains_domainId(domainId);
            goal.setFormsFormId(formId);
            goal.setGoal1(goalBody.getText().toString().trim());
            if (formId == Constants.FORM_4) {

                ArrayList<GoalsMetaDatum> goalsMetaData = new ArrayList<GoalsMetaDatum>();
                GoalsMetaDatum meta = new GoalsMetaDatum();
                GoalsMetaDatum proj1 = new GoalsMetaDatum();
                GoalsMetaDatum proj2 = new GoalsMetaDatum();
                GoalsMetaDatum proj3 = new GoalsMetaDatum();

                meta.setKey("مؤشر الهدف");
                meta.setValue(goalIndecator.getText().toString().trim());
                goalsMetaData.add(meta);
                goal.setGoalsMetaData(goalsMetaData);
                if(project1.getText().length()>0) {
                    proj1.setKey("المشروع الأول");
                    proj1.setValue(project1.getText().toString().trim());
                    goalsMetaData.add(proj1);

                }
                if(project2.getText().length()>0) {
                    meta.setKey("المشروع الثاني");
                    meta.setValue(project2.getText().toString().trim());
                    goalsMetaData.add(meta);
                    }
                if(project3.getText().length()>0) {
                    meta.setKey("المشروع الثالث");
                    meta.setValue(project3.getText().toString().trim());
                    goalsMetaData.add(meta);
                   }

                goal.setGoalsMetaData(goalsMetaData);
            }
                showProgress(true);

                if(!checkGoalIsNotExist(goalBody.getText().toString())){
                Call call = goalServices.AddNewGoal(goal);
            new LoadFormsTask().execute(call);
                }else {
                    showProgress(false);
                    Toast.makeText(getActivity(),getResources().getString(R.string.GoalAreadyExist), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private boolean checkGoalIsNotExist(String s) {
        boolean isExist = false;
        for(GoalModel goal : insertedGoals){
            if(goal.getGoal1().equals(s)){
                isExist =  true;
            }
        }
        return  isExist;

    }

    private boolean checkProjectsEditTxt(int formId) {
        boolean isValid = true;
        if(formId == Constants.FORM_4) {
            if (!(goalBody.getText().length() > 0)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.GoalShouldBeNotNull), Toast.LENGTH_LONG).show();
                isValid = false;
            } else if (!(goalIndecator.getText().length() > 0)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.IndecatroIsRequired), Toast.LENGTH_LONG).show();
                isValid = false;
            } else if (!(project1.getText().length() > 0 || project2.getText().length() > 0 || project3.getText().length() > 0)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.AtLeastGoalRequired), Toast.LENGTH_LONG).show();
                isValid = false;
            }
        }else {
            if (!(goalBody.getText().length() > 0)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.GoalShouldBeNotNull), Toast.LENGTH_LONG).show();
                isValid = false;
            }
        }

            return  isValid;

    }

    public class LoadFormsTask extends AsyncTask<Call, Void, GoalModel> {
        @Override
        protected GoalModel doInBackground(Call... params) {
            Call<GoalModel> c = params[0];
            GoalModel insertedGoal = null;
            try {
                Response<GoalModel> responseBody = c.execute();
                insertedGoal =responseBody.body();
                return  insertedGoal;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return  insertedGoal;
        }

        @Override
        protected void onPostExecute(GoalModel  goal) {
            showProgress(false);
            if(goalServices !=null) {
                Intent domainsIntent = new Intent(getActivity(), GoalsActivity.class);
                domainsIntent.putExtra("domainId",domainId);
                domainsIntent.putExtra("formId",formId);
                getActivity().startActivity(domainsIntent);
            }else {
                Toast.makeText(getActivity(), getResources().getString(R.string.SaveProccessIsValid), Toast.LENGTH_LONG).show();
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
