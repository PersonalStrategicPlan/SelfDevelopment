package com.api_l.forms.ListAdapters;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.api_l.forms.Dialogs.FormDialog;
import com.api_l.forms.Dialogs.NoticeDialogListener;
import com.api_l.forms.GoalActivity;
import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.Models.UserModel;
import com.api_l.forms.R;
import com.api_l.forms.UserInfoActivity;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<UserModel> implements View.OnClickListener, NoticeDialogListener {
    private ArrayList<UserModel> userModels = new ArrayList<UserModel>();
    private Context ctx;
    private Activity activity;
    public UsersAdapter(@NonNull Context context, @NonNull ArrayList<UserModel> objects, Activity activity) {
        super(context,0, objects);
        this.userModels = objects;
        ctx = context;
        this.activity = activity;
    }
    public UserModel getItem(int p){
        return  userModels.get(p);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }
        UserModel model = getItem(position);
        TextView btn = (TextView) convertView.findViewById(R.id.goalBodyTxt01);
        Button detail = (Button) convertView.findViewById(R.id.goalShowDetails);

        detail.setTag(position);
        detail.setOnClickListener(this);
        btn.setText(model.getUserFullName());
        btn.setTag(position);
        btn.setOnClickListener(this);
        btn.setLongClickable(true);
        return convertView;
    }

    @Override
    public void onClick(View v) {

        UserModel model = getItem((int)v.getTag());
        switch (v.getId()){

            case R.id.goalShowDetails:
                Intent intent = new Intent(this.activity,UserInfoActivity.class);
               intent.putExtra("userModel",model);
                ctx.startActivity(intent);
                break;

        }

    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

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
