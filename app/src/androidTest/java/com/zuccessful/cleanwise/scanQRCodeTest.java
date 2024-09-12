package com.zuccessful.cleanwise;

/**
 * Created by rajshreekhare on 30/4/18.
 */


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.espresso.Espresso;

import com.zuccessful.cleanwise.activities.HistoryActivity_MT17010;
import com.zuccessful.cleanwise.activities.ScanQRActivity_MT17010;
import com.zuccessful.cleanwise.activities.SignInActivity_MT17010;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
/**
 * Created by rajshreekhare on 30/4/18.
 */

@RunWith(AndroidJUnit4.class)

public class scanQRCodeTest {

    FirebaseUser user;

    @Rule
    public IntentsTestRule<ScanQRActivity_MT17010> atr = new IntentsTestRule<ScanQRActivity_MT17010>(ScanQRActivity_MT17010.class);

    @Before
    public void createUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    @Test
    public void checkSignin()
    {
        if(user != null) {
            Espresso.onView(ViewMatchers.withId(R.id.scan_screen_button)).perform(click());
            intended(hasComponent(HistoryActivity_MT17010.class.getName()));
        }
    }


}

