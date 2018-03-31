package com.api_l.forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.api_l.forms.Models.GoalModel;
import com.api_l.forms.Models.UserModel;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
   private TextView fnTxt,mTxt,eTxt,spTxt,empTxt,acTxt,qfTxt,expTxt;
private UserModel userModel;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt("Target",userModel.getUserid());
        editor.commit();
        Intent i  =new Intent(this,FormsActivity.class);
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                MoveToProfile();
                return true;
            case R.id.logout:
                Logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Logout() {
        this.sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        this.sharedPreferences.edit().clear().commit();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);

    }

    private void MoveToProfile() {
        Intent  i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

}
