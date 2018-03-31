package com.api_l.forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.api_l.forms.APIs.ApiUtils;
import com.api_l.forms.APIs.UserService;
import com.api_l.forms.Models.AuthModel;
import com.api_l.forms.Models.UserModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sharedPreferences;
    private EditText fnTxt,mTxt,eTxt,spTxt,empTxt,acTxt,qfTxt,expTxt;
    private  String FullName ="",mobile ="",email="",sp="",empName="",Acacdmic="",QF="",EXP="";
    private int userid=0;
    private Button updateBtn;
    private UserService userServiceService;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt("UserId",0);
        FullName = sharedPreferences.getString("UserFullName","");
        Acacdmic = sharedPreferences.getString("UserAcadmicID","");
        email = sharedPreferences.getString("email","");
        QF = sharedPreferences.getString("QF","");
        EXP = sharedPreferences.getString("EXP","");
        empName = sharedPreferences.getString("empName","");
        sp =  sharedPreferences.getString("sp","");
        mobile =  sharedPreferences.getString("mobile","");

        updateBtn = (Button) findViewById(R.id.saveProfile);
        updateBtn.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        fnTxt = (EditText) findViewById(R.id.fullNametxt);
        fnTxt.setText(FullName);
        eTxt = (EditText) findViewById(R.id.emailTxt);
        eTxt.setText(email);
        mTxt = (EditText) findViewById(R.id.mobileTxt);
        mTxt.setText(mobile);
        spTxt = (EditText) findViewById(R.id.spTxt);
        spTxt.setText(sp);
        //spTxt.setEnabled(false);
        empTxt = (EditText) findViewById(R.id.empNameTxt);
        empTxt.setText(empName);
       // empTxt.setEnabled(false);
        acTxt = (EditText) findViewById(R.id.acadmicID);
        acTxt.setText(Acacdmic);
        acTxt.setEnabled(false);
        qfTxt = (EditText) findViewById(R.id.QFTxt);
        qfTxt.setText(QF);
        expTxt = (EditText) findViewById(R.id.EXPTxt);
        expTxt.setText(EXP);
showProgress(false);
        userServiceService = ApiUtils.getUserService();

    }
    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public final static boolean isValidMobile(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }
    public final static boolean isValidQF(CharSequence target) {
        return (!TextUtils.isEmpty(target));
    }
    public void doUpdate() {
        showProgress(true);
        UserModel userModel = new UserModel();
        userModel.setEmail((isValidEmail(eTxt.getText().toString().trim())? eTxt.getText().toString().trim():email));
        userModel.setMobile((isValidMobile(mTxt.getText().toString().trim())? mTxt.getText().toString().trim():mobile));
        userModel.setUserAcadmicID(Acacdmic);
        userModel.setEmpName(empName);
        userModel.setuserExp((isValidQF(expTxt.getText().toString().trim())? expTxt.getText().toString().trim():EXP));
        userModel.setSp(sp);
        userModel.setUserid(userid);
        userModel.setQf((isValidQF(qfTxt.getText().toString().trim())? qfTxt.getText().toString().trim():QF));
       userModel.setUserFullName((isValidQF(fnTxt.getText().toString().trim())? fnTxt.getText().toString().trim():FullName));

        Call call = userServiceService.UpdateProfile(userModel);
        new ProfileActivity.UpdateUserProfileTask().execute(call);

    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.saveProfile){
            doUpdate();
        }
    }
    private void Move() {
        Intent refreshActivity = new Intent(this,ProfileActivity.class);
        startActivity(refreshActivity);
    }

    public class UpdateUserProfileTask extends AsyncTask<Call, Void, UserModel> {
        @Override
        protected UserModel doInBackground(Call... params) {
            Call<UserModel> c = params[0];

            Response<UserModel> responseBody = null;
            try {
                responseBody = c.execute();
                UserModel loggedInUser =responseBody.body();
                return  loggedInUser;

            } catch (IOException e) {
                e.printStackTrace();
            }


            return  new UserModel("","");
        }

        @Override
        protected void onPostExecute(UserModel  loggedInUser) {
            showProgress(false);
            if(loggedInUser!=null) {
                if(loggedInUser.getUserFullName() == null){
                    Toast.makeText(ProfileActivity.this, "حاول مرة أخرى هناك مشكلة بالاتصال", Toast.LENGTH_LONG).show();
                }
                //  Log.d("OBJECT: ", loggedInUser.getUserFullName());
                else{
                    Toast.makeText(ProfileActivity.this, "تم التعديل بنجاح! " , Toast.LENGTH_LONG).show();

                    sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putInt("UserId",loggedInUser.getUserid());
                    editor.putString("UserFullName",loggedInUser.getUserFullName());
                    editor.putString("UserAcadmicID",loggedInUser.getUserAcadmicID());
                    editor.putString("QF",loggedInUser.getQf());
                    editor.putString("EXP",loggedInUser.getExp());
                    editor.putString("mobile",loggedInUser.getMobile());
                    editor.putString("sp",loggedInUser.getSp());//empName
                    editor.putString("email",loggedInUser.getUserEmail());//empName
                    editor.putString("empName",loggedInUser.getEmpName());//empName
                    editor.commit();


                  // Move();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "فضلاً تأكد من بياناتك المدخلة", Toast.LENGTH_LONG).show();
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
