package com.home.coexcleducation.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.home.coexcleducation.MainActivity;
import com.home.coexcleducation.R;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.ui.onboarding.OnBoardingActivity;


public class LaunchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);
        LinearLayout lLayout = findViewById(R.id.parent);

        if(getApplicationContext().getPackageName().equals("com.home.coexcleducation")) {
            lLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.coexcl_background));
        } else {
            lLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.coexcl_background_cofi));
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent lIntent = null;
                if(UserDetails.getInstance().isLoggedIn()) {
                    lIntent = new Intent(LaunchActivity.this, MainActivity.class);
                } else {
                    lIntent = new Intent(LaunchActivity.this, OnBoardingActivity.class);
                }
                startActivity(lIntent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        }, 1000);

    }

}
