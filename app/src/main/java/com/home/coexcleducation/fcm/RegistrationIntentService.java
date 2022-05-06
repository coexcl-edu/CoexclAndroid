package com.home.coexcleducation.fcm;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.freshchat.consumer.sdk.Freshchat;
import com.google.firebase.iid.FirebaseInstanceId;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.Constants;
import com.home.coexcleducation.utils.PreferenceHelper;

import io.intercom.android.sdk.push.IntercomPushClient;

/**
 * Created by ABHIshek on 23/05/16.
 */
public class RegistrationIntentService extends JobIntentService {

    private static final String TAG = "RegistrationIntentService";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        if (UserDetails.getInstance().isLoggedIn()) {
            String refreshedToken = "";
            try {
                refreshedToken = FirebaseInstanceId.getInstance().getToken(Constants.FCM_REGISTRATION_KEY, "FCM");
            } catch (Exception e) {
                CoexclLogs.printException(e);
            }
            Freshchat.getInstance(this).setPushRegistrationToken(refreshedToken);
            new IntercomPushClient().sendTokenToIntercom(getApplication(), refreshedToken);
            CoexclLogs.errorLog(TAG, "   Sending FCM token to intercom   " + refreshedToken);
            CoexclLogs.infoLog(TAG, "Refreshed token: " + refreshedToken);
            CoexclLogs.infoLog(TAG, "fcm token: " + refreshedToken);
        }
    }

}


