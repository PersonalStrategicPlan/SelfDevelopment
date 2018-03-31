package com.api_l.forms.ListAdapters;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.api_l.forms.Dialogs.FormDialog;
import com.api_l.forms.Dialogs.NoticeDialogListener;
import com.api_l.forms.Models.DomainModel;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.R;

import java.util.ArrayList;


public class GoalsAdapter extends ArrayAdapter<GoalModel> implements View.OnClickListener, NoticeDialogListener {



        private ArrayList<GoalModel> goals = new ArrayList<GoalModel>();
        private int UserId =1 ;
        private Context ctx;
    private  Activity activity;
        public GoalsAdapter(@NonNull Context context, @NonNull ArrayList<GoalModel> objects,Activity activity) {
            super(context,0, objects);
            this.goals = objects;
            ctx = context;
            this.activity = activity;
        }
        public GoalModel getItem(int p){
            return  goals.get(p);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.generic_list_adapter, parent, false);
            }
            GoalModel model = getItem(position);
            TextView btn = (TextView) convertView.findViewById(R.id.itemTitle);
            btn.setText(model.getGoalId()+" - "+model.getGoal1());
            btn.setTag(position);
            btn.setOnClickListener(this);
            return convertView;
        }

        @Override
        public void onClick(View v) {
            GoalModel model = getItem((int)v.getTag());
            if(model!=null){
                FragmentManager fm = activity.getFragmentManager();
                FormDialog f = new FormDialog();
                f.setGoalId(model.getGoalId());
                f.show(fm, "flag");
               }
        }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
