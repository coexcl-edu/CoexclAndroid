package com.home.coexcleducation.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.transition.Fade;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.home.coexcleducation.R;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ViewUtils {

        boolean mInvalidDate = false;


        public void displayToast(String toastMessage , String type , final Activity pActivity, final String pCloseDirection) {

            int lToastTime = Toast.LENGTH_SHORT;
            try {
                pActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Dialog dialog = new Dialog(pActivity, R.style.BottomDialogTheme);
                LayoutInflater lInflater = (LayoutInflater) pActivity.getApplication().getSystemService(LAYOUT_INFLATER_SERVICE);
                View lLayout = lInflater.inflate(R.layout.alert_toast, null);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(lLayout);
                TextView lText = (TextView) dialog.findViewById(R.id.Toast_Message);
                lText.setText(toastMessage);

                ProgressBar lProgress = (ProgressBar) dialog.findViewById(R.id.Toast_Spinner);
                lProgress.setVisibility(View.GONE);

                ImageView lImage = (ImageView) dialog.findViewById(R.id.Toast_Image);
                lImage.setPadding(15, 15, 15, 15);
                if("success".equalsIgnoreCase(type)) {
                    lImage.setImageResource(R.drawable.ic_check_blue_bg_lightblue);
                } else if("failure".equalsIgnoreCase(type)) {
                    lImage.setImageResource(R.drawable.ic_close_red_bg_red);
                } else if("long".equalsIgnoreCase(type)){
                    lToastTime = Toast.LENGTH_LONG;
                }
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setEnterTransition(new Fade());
                }
                WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
                attributes.y = 200;
                dialog.getWindow().setAttributes( attributes );
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if(pActivity != null) {

                                    pActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (!pActivity.isFinishing() && dialog != null && dialog.isShowing())
                                                dialog.dismiss();

                                            pActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            if (pCloseDirection.equals("edit")) {
                                                exitActivityToRight(pActivity);
                                            } else if (pCloseDirection.equals("create")) {
                                                exitActivityToBottom(pActivity);
                                            }
                                        }
                                    });
                                }

                            }
                        }, 3000);
                dialog.show();
            } catch (Exception e) {
                CoexclLogs.printException(e);
            }
        }

        public Dialog displayProgressDialog(String toastMessage , Context context) {

            Dialog lProgressDialog = null;
            try {
                LayoutInflater lInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View lLayout = lInflater.inflate(R.layout.alert_progress , null);
                // set a message
//                ImageView lLoader = lLayout.findViewById(R.id.loader);

                TextView lText = (TextView) lLayout.findViewById(R.id.Toast_Message);
                lText.setText(toastMessage);
//                Glide.with(context).asGif()
//                        .load(R.raw.setmore_loader)
//                        .into(lLoader);


                lProgressDialog = new Dialog(context,R.style.DialogCustomTheme);
                lProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                lProgressDialog.setContentView(lLayout);

                Window window = lProgressDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lProgressDialog.show();
                lProgressDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                lProgressDialog.setCanceledOnTouchOutside(false);
                lProgressDialog.setCancelable(false);

            } catch (Exception e) {
                CoexclLogs.printException(e);
            }
            return lProgressDialog;
        }



    public void exitActivityToBottom(Activity activity) {
        try {
            activity.finish();
            activity.overridePendingTransition(R.anim.fixed_position, R.anim.slide_in_top);
        } catch (Exception e) {

            CoexclLogs.printException(e);
        }
    }


    public void exitActivityToRight(Activity activity){
        try {
            CoexclLogs.infoLog("ViewUtilites", "exitActivityToRight");
            activity.finish();
            activity.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

    public void exitActivityToLeft(Activity activity) {
        try {
            CoexclLogs.infoLog("ViewUtilites", "exitActivityToLeft");
            activity.finish();
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

    public void moveViewToUp(View view, int pScreenHeight) {
        try
        {
            Animation lAnimation 	= new TranslateAnimation(0, 0, pScreenHeight, 0);
            lAnimation.setDuration(500);
            lAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            view.startAnimation(lAnimation);
        }
        catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

    public void moveViewToRight(View view, int pScreenHeight) {
        try
        {
            Animation lAnimation 	= new TranslateAnimation(pScreenHeight, 0, 0, 0);
            lAnimation.setDuration(500);
            lAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            view.startAnimation(lAnimation);
        }
        catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

    public void moveViewToLeft(View view, int pScreenHeight) {
        try
        {
            Animation lAnimation 	= new TranslateAnimation(0, pScreenHeight, 0, 0);
            lAnimation.setDuration(500);
            lAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            view.startAnimation(lAnimation);
        }
        catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

    public void moveViewToLeft(View toleft , View fromright, int pScreenWidth) {
        try
        {
            Animation lAnimationFromRight 	= new TranslateAnimation(pScreenWidth, 0, 0, 0);
            Animation lAnimationToLeft 		= new TranslateAnimation(0, -pScreenWidth, 0, 0);
            lAnimationFromRight.setDuration(350);
            lAnimationToLeft.setDuration(350);
            lAnimationFromRight.setInterpolator(new AccelerateDecelerateInterpolator());
            lAnimationToLeft.setInterpolator(new AccelerateDecelerateInterpolator());
            fromright.startAnimation(lAnimationFromRight);
            toleft.startAnimation(lAnimationToLeft);
        }
        catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

    public void moveViewToRight(View fromleft , View toright, int pScreenWidth) {
        try
        {
            Animation lAnimationFromLeft 	= new TranslateAnimation(0, pScreenWidth, 0, 0);
            Animation lAnimationToRight 	= new TranslateAnimation(-pScreenWidth, 0, 0, 0);
            lAnimationFromLeft.setDuration(350);
            lAnimationToRight.setDuration(350);
            fromleft.startAnimation(lAnimationFromLeft);
            lAnimationFromLeft.setInterpolator(new AccelerateDecelerateInterpolator());
            lAnimationToRight.setInterpolator(new AccelerateDecelerateInterpolator());
            toright.startAnimation(lAnimationToRight);
        }
        catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }


    public void setWindowBackground(Activity lActivity) {

        Window window= lActivity.getWindow();
        Drawable background = lActivity.getResources().getDrawable(R.drawable.appcolour_gradient_for_background);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(lActivity.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
        window.setNavigationBarColor(ContextCompat.getColor(lActivity, R.color.colorPrimaryDark));
    }

    public void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri ) {
        String packageName = "com.android.chrome";
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}


