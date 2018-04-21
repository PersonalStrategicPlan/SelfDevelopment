package com.api_l.forms.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.usage.ConfigurationStats;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.AnswerServices;
import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.Helper.Constants;
import com.api_l.forms.Models.AnswerModel;
import com.api_l.forms.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;


public class FormDialog extends DialogFragment implements NoticeDialogListener, View.OnClickListener {
    private ImageButton soHappyBtn,happyBtn,avreageBtn,notHappyBtn,sadBtn;
    private AnswerServices answerService;
    private ProgressBar progressBar;
    private boolean isFiveLevels = true;
    private int goalId;
    private SharedPreferences sharedPreferences;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
        View promptView = layoutInflater.inflate(R.layout.form_dialog_layout, null);

        answerService = ApiUtils.getAnswers();
        progressBar = (ProgressBar) promptView.findViewById(R.id.dialogProgressBar);
        showProgress(false);
        soHappyBtn = (ImageButton) promptView.findViewById(R.id.SoHappyBtn);
        soHappyBtn.setOnClickListener(this);
        happyBtn = (ImageButton) promptView.findViewById(R.id.HappyBtn);
        happyBtn.setOnClickListener(this);
        avreageBtn = (ImageButton) promptView.findViewById(R.id.AverageBtn);
        avreageBtn.setOnClickListener(this);
        notHappyBtn = (ImageButton) promptView.findViewById(R.id.NotHappyBtn);
        notHappyBtn.setOnClickListener(this);
        sadBtn = (ImageButton) promptView.findViewById(R.id.SadBtn);
        sadBtn.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("فضلاً قم بتقييم الهدف").setTitle("تقييم الأهداف")
                .setView(promptView)
                .setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        onDialogPositiveClick(FormDialog.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    public boolean isFiveLevels() {
        return isFiveLevels;
    }

    public void setFiveLevels(boolean fiveLevels) {
        isFiveLevels = fiveLevels;
    }

    public class AddAnswerTask extends AsyncTask<Call, Void, AnswerModel> {
        @Override
        protected AnswerModel doInBackground(Call... params) {
            Call<AnswerModel> c = params[0];

            Response<AnswerModel> responseBody = null;
            try {
                responseBody = c.execute();
                AnswerModel insertedAnswer =responseBody.body();
                return  insertedAnswer;

            } catch (IOException e) {
                e.printStackTrace();
            }


            return  new AnswerModel();
        }

        @Override
        protected void onPostExecute(AnswerModel  insertedAnswer) {
            showProgress(false);
            if(insertedAnswer!=null) {
                if(insertedAnswer.getColor() > 0){
                    Toast.makeText(getActivity(), getResources().getString(R.string.EvaluationDone), Toast.LENGTH_LONG).show();
                }

                else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.EvaluationUpdated) , Toast.LENGTH_LONG).show();


                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.TryAgain), Toast.LENGTH_LONG).show();
            }
        }



        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }
    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {

            NoticeDialogListener mn = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
                       throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
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
            case  R.id.SoHappyBtn:
                addAnswer(Constants.SoHappyRank);
                break;
            case R.id.HappyBtn:
                addAnswer(Constants.HappyRank);
                break;
            case R.id.AverageBtn:
                addAnswer(Constants.AvarageRank);
                break;
            case R.id.NotHappyBtn:
                addAnswer(Constants.NotHappyRank);
                break;
            case R.id.SadBtn:
                addAnswer(Constants.SadRank);
                break;
        }

    }

    private  void addAnswer(int color){
        showProgress(true);
        this.sharedPreferences  = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        int userid = sharedPreferences.getInt("UserId",0);
        AnswerModel answerModel = new AnswerModel();
        answerModel.setColor(color);
        answerModel.setGoals_goalId(this.goalId);
        answerModel.setUsers_userid(userid);
        Call call = answerService.AddAnswer(answerModel);
        new AddAnswerTask().execute(call);

    }
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ==false ? View.GONE : View.VISIBLE);

    }
}
