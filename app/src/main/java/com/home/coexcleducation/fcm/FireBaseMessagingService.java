package com.home.coexcleducation.fcm;

import android.content.SharedPreferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshchat.consumer.sdk.Freshchat;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.PreferenceHelper;
import com.home.coexcleducation.utils.Utilty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.intercom.android.sdk.push.IntercomPushClient;


/**
 * Created by ABHIshek on 08/02/17.
 */

public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FireBaseMessagingService";
    SharedPreferences mPreference;
    private final IntercomPushClient intercomPushClient = new IntercomPushClient();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mPreference = PreferenceHelper.getSharedPreference(this);

        CoexclLogs.errorLog(TAG, "======================= FCM Message Received ============================");
        CoexclLogs.errorLog(TAG, "======================= remoteMessage = " + remoteMessage.getData());
        try {
            if(UserDetails.getInstance().isLoggedIn()) {
                Map message = remoteMessage.getData();
                if (Freshchat.isFreshchatNotification(remoteMessage)) {
                    Freshchat.handleFcmMessage(this, remoteMessage);
                }
                if (intercomPushClient.isIntercomPush(message)) {
                    intercomPushClient.handlePush(getApplication(), message);
                    CoexclLogs.errorLog("TAG", "Msg from Intercom");
                }
            }
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }


    @Override
    public void onNewToken(String token) {
        CoexclLogs.infoLog(TAG, "new fcm token: " + token);
        if (UserDetails.getInstance().isLoggedIn()) {
            new IntercomPushClient().sendTokenToIntercom(getApplication(), token);
            try{
                new Utilty().updateToken(this ,token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showNotification(Map<String, String> lData) {
        CoexclLogs.infoLog(getClass().getName(), "showApptNotification");
        try {
            if (lData != null && !lData.isEmpty()) {
                ObjectMapper lMapper = new ObjectMapper();
                Map<String, ArrayList<String>> lReminderMap = lMapper.readValue(lData.get("data"), HashMap.class);
                String lkey = lReminderMap.containsKey("reminder") ? lReminderMap.get("reminder").get(0) : "";

                if (!lkey.equals("")) {
                    CoexclLogs.infoLog(getClass().getName(), "showApptNotification===");
//                    Intent lIntent = new Intent(this, ReminderAlarmReciever.class);
//                    lIntent.putExtra("reminderKey", lkey);
//                    sendBroadcast(lIntent);
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(lIntent);
                }
            }
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
    }

}
