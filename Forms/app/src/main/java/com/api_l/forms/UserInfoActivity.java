package com.api_l.forms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.Models.Role;
import com.api_l.forms.Models.UserModel;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
   private TextView fnTxt,mTxt,eTxt,spTxt,empTxt,acTxt,qfTxt,expTxt;
private UserModel userModel;
    SharedPreferences sharedPreferences;
    private int RoleId =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        RoleId = sharedPreferences.getInt("RoleId",0);
        int target = sharedPreferences.getInt("Target",0);
        int userId = sharedPreferences.getInt("UserId",0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(userId == target){
            fab.setVisibility(View.GONE);
        }
        Button updateProfile = (Button) findViewById(R.id.updateProfileBtn);
        updateProfile.setOnClickListener(this);
        fab.setOnClickListener(this);
        updateProfile.setVisibility(View.GONE);

        if(RoleId !=2){
            fab.setVisibility(View.GONE);
        }if((userId==target)){
            updateProfile.setVisibility(View.VISIBLE);

        }
        Intent i = getIntent();
        userModel = (UserModel) i.getSerializableExtra("userModel");
        fnTxt = (TextView) findViewById(R.id.userFullNameInf);
        fnTxt.setText("الاسم :"+userModel.getUserFullName());
        eTxt = (TextView) findViewById(R.id.userEmail);
        eTxt.setText("البريد الالكتروني :"+userModel.getEmail());
        mTxt = (TextView) findViewById(R.id.userMobile);
        mTxt.setText("رقم الجوال :"+userModel.getMobile());
        spTxt = (TextView) findViewById(R.id.userSp);
        spTxt.setText("التخصص :"+userModel.getSp());
        empTxt = (TextView) findViewById(R.id.userEmpName);
        empTxt.setText("المسمى الوظيفي :"+userModel.getEmpName());
        acTxt = (TextView) findViewById(R.id.userAcadmicInf);
        acTxt.setText("الرقم الأكاديمي :"+userModel.getUserAcadmicID());
        qfTxt = (TextView) findViewById(R.id.userQF);
        qfTxt.setText("المؤهل الدراسي :"+userModel.getQf());
        expTxt = (TextView) findViewById(R.id.userExp);
        expTxt.setText("الخبرات والمهارات :"+"\n"+userModel.getExp());



        }
    private void ShowColorMeaningDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.color_meaning_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(getResources().getString(R.string.EvaluationRanksMeaning));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.HIDE), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.fab:
                sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putInt("Target",userModel.getUserid());
                editor.commit();
                Intent i  =new Intent(this,FormsActivity.class);
                startActivity(i);
                break;
            case R.id.updateProfileBtn:
                Intent updateProfileIntent = new Intent(this,ProfileActivity.class);
                startActivity(updateProfileIntent);
                break;
        }

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
            case  R.id.forms:
                MoveToForms();
                return  true;
            case R.id.logout:
                Logout();
            case  R.id.colormeaning:
                ShowColorMeaningDialog();
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
