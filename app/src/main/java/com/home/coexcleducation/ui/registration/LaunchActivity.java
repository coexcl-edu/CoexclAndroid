package com.home.coexcleducation.ui.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.home.coexcleducation.R;
import com.home.coexcleducation.ui.onboarding.OnBoardingActivity;


public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent lIntent = new Intent(LaunchActivity.this, OnBoardingActivity.class);
        startActivity(lIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }

}
