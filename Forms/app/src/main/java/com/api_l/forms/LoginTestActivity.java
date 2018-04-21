package com.api_l.forms;

import com.api_l.forms.LoginActivity;
import com.api_l.forms.Models.UserModel;



public class LoginTestActivity extends LoginActivity {
    private LoginTestCallback mCallback;



    public void setLoginCallback(LoginTestCallback loginCallback){
        mCallback = loginCallback;
    }

    public interface LoginTestCallback{
        void onHandleResponseCalled(UserModel loggedInUser);
    }
    @Override
    public void Move(UserModel loggedInUser){

    }
}
