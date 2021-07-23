package com.rosemellow.alternatecreditscore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    SavePref savePref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        savePref = new SavePref(SplashActivity.this);


Log.e("Feqasdcfedc",savePref.getMyPhoneNumber()+"abc");




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                startActivity(new Intent(SplashActivity.this, TermsandConditionsActivity.class));
                finish();


            }
        }, 2000);
    }
}