package com.api_l.forms.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AddNewGoalDialog extends DialogFragment implements NoticeDialogListener, View.OnClickListener {
    private ProgressBar progressBar;
    private int goalId;
    private Button saveBtn;
    private GoalServices goalServices;
    private EditText goalBody,goalIndecator,project1,project2,project3;
    private Integer formId,domainId;
    private int userId = 0;

    private TextView txtIndecator ,projectslbl;
    public AddNewGoalDialog(int userId, Integer formId, Integer domainId) {
        this.userId = userId;
        this.domainId = domainId;
        this.formId =formId;
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
        goalIndecator =(EditText) promptView.findViewById(R.id.goalindecator);
        txtIndecator = (TextView) promptView.findViewById(R.id.textView3);
        projectslbl = (TextView) promptView.findViewById(R.id.projectsLbl);
        project1 = (EditText) promptView.findViewById(R.id.project1);
        project2 = (EditText) promptView.findViewById(R.id.project2);
        project3 = (EditText) promptView.findViewById(R.id.project3);
        if(formId == 3){
            goalIndecator.setVisibility(View.GONE);
            txtIndecator.setVisibility(View.GONE);
            projectslbl.setVisibility(View.GONE);
            project1.setVisibility(View.GONE);
            project2.setVisibility(View.GONE);
            project3.setVisibility(View.GONE);
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
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.savebtn:
                addGoal();
                break;
        }
    }

    private  void addGoal(){
        showProgress(true);
        if(!checkProjectsEditTxt() && formId == 4){
            showProgress(false);
            Toast.makeText(getActivity(),"يجب إدخال مشروع واحد على الأقل",Toast.LENGTH_SHORT).show();
        }else {
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


            Call call = goalServices.AddNewGoal(goal);
            new LoadFormsTask().execute(call);
        }

    }

    private boolean checkProjectsEditTxt() {
        if(project1.getText().length() >0 || project2.getText().length() >0 || project3.getText().length() >0){

            return  true;
        }
        return  false;
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
                //  Toast.makeText(FormsActivity.this, "Try again please!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return  insertedGoal;
        }

        @Override
        protected void onPostExecute(GoalModel  goal) {
            showProgress(false);
            if(goalServices !=null) {
                // Toast.makeText(getActivity(), "تم الحفظ بنجاح", Toast.LENGTH_LONG).show();


                Intent domainsIntent = new Intent(getActivity(), GoalsActivity.class);
                domainsIntent.putExtra("domainId",domainId);
                // int d = selectedItem.getDomainId();
                domainsIntent.putExtra("formId",formId);
                getActivity().startActivity(domainsIntent);
            }else {
                Toast.makeText(getActivity(), "لم يتم الحفظ !", Toast.LENGTH_LONG).show();
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
