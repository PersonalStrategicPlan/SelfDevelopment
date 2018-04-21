package com.api_l.forms;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.api_l.forms.Models.UserModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class TestAsynApiConnection {

    private static final String TAG = LoginTestActivity.class.getSimpleName();

    @Rule
    public ActivityTestRule<LoginTestActivity> mActivityRule = new ActivityTestRule<>(LoginTestActivity.class);
    @Test
    public void testLoginViaService() throws Exception {
        /** 1.*/
        onView(withId(R.id.userName)).perform(typeText("1422818"));
        /** 2.*/
     //   onView(withId(R.id.userPass)).perform(typeText("000000"));

        final Object syncObject = new Object();

        LoginTestActivity loginActivity = mActivityRule.getActivity();
        loginActivity.setLoginCallback(new LoginTestActivity.LoginTestCallback() {
            @Override
            public void onHandleResponseCalled(UserModel loginResponse) {
                Log.v(TAG, "onHandleResponseCalled in thread " + Thread.currentThread().getId());

                assertTrue(loginResponse.getUserFullName() != null);
                synchronized (syncObject){
                    syncObject.notify();
                }
            }
        });

        /** 3.*/
        onView(withId(R.id.loginBtn)).perform(click());

        /** 4.
         * Here is a synchronization on local variable but it's ok.
         */
        synchronized (syncObject){
            syncObject.wait();
        }
    }

}
