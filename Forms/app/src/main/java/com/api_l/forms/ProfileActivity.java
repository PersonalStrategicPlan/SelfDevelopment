package com.api_l.forms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
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
    private String validationMsg = "";
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

        empTxt = (EditText) findViewById(R.id.empNameTxt);
        empTxt.setText(empName);

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
        return (!TextUtils.isEmpty(target) &&target.toString().indexOf("@")>-1 && target.toString().indexOf(".")>-1);
    }
    public final static boolean isValidMobile(CharSequence target) {
        return (!TextUtils.isEmpty(target) && target.length() == 10 && Patterns.PHONE.matcher(target).matches());
    }
    public final static boolean isValidQF(CharSequence target) {
        return (!TextUtils.isEmpty(target));
    }
    public void doUpdate() {
        if(!checkInput()){
            Toast.makeText(this,validationMsg,Toast.LENGTH_LONG).show();
        }else {
            showProgress(true);
            UserModel userModel = new UserModel();
            userModel.setEmail((isValidEmail(eTxt.getText().toString().trim()) ? eTxt.getText().toString().trim() : email));
            userModel.setMobile((isValidMobile(mTxt.getText().toString().trim()) ? mTxt.getText().toString().trim() : mobile));
            userModel.setUserAcadmicID(Acacdmic);
            userModel.setEmpName((isValidQF(empTxt.getText().toString().trim()) ? empTxt.getText().toString().trim() : empName));
            userModel.setuserExp((isValidQF(expTxt.getText().toString().trim()) ? expTxt.getText().toString().trim() : EXP));
            userModel.setSp((isValidQF(spTxt.getText().toString().trim()) ? spTxt.getText().toString().trim() : sp));
            userModel.setUserid(userid);
            userModel.setQf((isValidQF(qfTxt.getText().toString().trim()) ? qfTxt.getText().toString().trim() : QF));
            userModel.setUserFullName((isValidQF(fnTxt.getText().toString().trim()) ? fnTxt.getText().toString().trim() : FullName));

            Call call = userServiceService.UpdateProfile(userModel);
            new ProfileActivity.UpdateUserProfileTask().execute(call);
        }
    }

    private boolean checkInput() {
        validationMsg ="";
        boolean isValid = true;
        if(!isValidQF(fnTxt.getText().toString().trim())){
            validationMsg = validationMsg+"الاسم يجب ألا يكون فارغا";
            isValid = false;
            fnTxt.setFocusable(true);
        }
        else if(!isValidEmail(eTxt.getText().toString().trim())){
            validationMsg = validationMsg+"الايميل غير صالح";
            isValid = false;
            expTxt.setFocusable(true);
        }else if(!isValidMobile(mTxt.getText().toString().trim())){
            validationMsg = validationMsg+"الجوال غير صالح";
            isValid = false;
            mTxt.setFocusable(true);
        }else if(!isValidQF(empTxt.getText().toString().trim())){
            validationMsg = validationMsg+"المسمى الوظيفي غير صالح" ;
            isValid = false;
            empTxt.setFocusable(true);
        }
        else  if(!isValidQF(expTxt.getText().toString().trim())){
            validationMsg = validationMsg+"الخبرات والمهارات مدخلة بشكل غير صحيح";
            isValid = false;
            expTxt.setFocusable(true);
        }
        else if(!isValidQF(spTxt.getText().toString().trim())){
            validationMsg = validationMsg+"التخصص مدخل بطريقة غير صحيحة";
            isValid = false;
            spTxt.setFocusable(true);

        }else if(!isValidQF(qfTxt.getText().toString().trim())){
            validationMsg = validationMsg+"المؤهلات مدخلة بطريقة غير صحيحة" ;
            isValid = false;
            qfTxt.setFocusable(true);
        }
        return  isValid;
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
    private void ShowColorMeaningDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.color_meaning_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("معاني ألوان التقييم");
        dialogBuilder.setNegativeButton("إخفاء", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
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
                    editor.putString("email",loggedInUser.getEmail());//empName
                    editor.putString("empName",loggedInUser.getEmpName());//empName
                    editor.commit();
                    MoveToProfile();
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
