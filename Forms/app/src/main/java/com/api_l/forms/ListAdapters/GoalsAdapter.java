package com.api_l.forms.ListAdapters;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.api_l.forms.Dialogs.FormDialog;
import com.api_l.forms.Dialogs.NoticeDialogListener;
import com.api_l.forms.GoalActivity;
import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.R;

import java.util.ArrayList;


public class GoalsAdapter extends ArrayAdapter<GoalModel> implements View.OnClickListener, View.OnLongClickListener, NoticeDialogListener {

    private SharedPreferences sharedPreferences;
        private ArrayList<GoalModel> goals = new ArrayList<GoalModel>();

    private  Integer formId =0;
        private Context ctx;
    private  Activity activity;
        public GoalsAdapter(@NonNull Context context, @NonNull ArrayList<GoalModel> objects,Activity activity,Integer formId) {
            super(context,0, objects);
            this.goals = objects;
            ctx = context;
            this.activity = activity;
             this.formId = formId;
        }
        public GoalModel getItem(int p){
            return  goals.get(p);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
            }
            GoalModel model = getItem(position);
            TextView btn = (TextView) convertView.findViewById(R.id.goalBodyTxt01);
            Button detail = (Button) convertView.findViewById(R.id.goalShowDetails);
            if(formId < 3){
                detail.setVisibility(View.GONE);
            }

            detail.setTag(position);
            detail.setOnClickListener(this);
            btn.setText(model.getGoalId()+" - "+model.getGoal1());
            btn.setTag(position);
            btn.setOnClickListener(this);
            btn.setLongClickable(true);
            btn.setOnLongClickListener(this);
            return convertView;
        }

        @Override
        public void onClick(View v) {

            GoalModel model = getItem((int)v.getTag());
            this.sharedPreferences = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            int roleId = sharedPreferences.getInt("RoleId",1);

            switch (v.getId()){

                case R.id.goalShowDetails:
                    Intent intent = new Intent(this.activity,GoalActivity.class);
                    intent.putExtra("goal",model);
                    this.activity.startActivity(intent);
                    break;
                case R.id.goalBodyTxt01:
                    if(model!=null && formId != 3 && roleId !=2){
                        FragmentManager fm = activity.getFragmentManager();
                        FormDialog f = new FormDialog();
                        f.setGoalId(model.getGoalId());
                        f.show(fm, "flag");
                    }
                    break;

            }

        }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }



    @Override
    public boolean onLongClick(View v) {
        GoalModel goalModel = new GoalModel();
        goalModel = getItem((int)v.getTag());
        Intent intent = new Intent(this.activity,GoalActivity.class);
        intent.putExtra("goal",goalModel);
         ctx.startActivity(intent);
        return false;

    }

    @org.jetbrains.annotations.Contract("null -> false")
    private boolean MoveToGoalActivity(GoalModel goalModel)
    {

       // GoalActivity activity = new GoalActivity(goalModel);
            Intent intent = new Intent(this.activity,GoalActivity.class);
            intent.putExtra("goal",goalModel);
            ctx.startActivity(intent);
            return true;

    }
}
