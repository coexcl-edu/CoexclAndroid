package com.home.coexcleducation;

import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.home.coexcleducation.intercom.IntercomHelper;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.utils.ApiConstant;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

public class MainApplication extends MultiDexApplication {

    public static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ApiConstant.ONE_SIGNAL_KEY);

        new IntercomHelper().initialize(this);
        new IntercomHelper().regsisterIntercomUser(UserDetails.getInstance().getID());
        new IntercomHelper().updateUser(MainApplication.CONTEXT);

        OneSignal.setNotificationWillShowInForegroundHandler(new OneSignal.OSNotificationWillShowInForegroundHandler() {
            @Override
            public void notificationWillShowInForeground(OSNotificationReceivedEvent notificationReceivedEvent) {

            }
        });



//        IntentFilter lFilter = new IntentFilter(Freshchat.FRESHCHAT_EVENTS);
//        LocalBroadcastManager.getInstance(CONTEXT).registerReceiver(lReceiver, lFilter);


//        OneSignal.setNotificationOpenedHandler(
//                new OneSignal.OSNotificationOpenedHandler() {
//                    @Override
//                    public void notificationOpened(OSNotificationOpenedResult result) {
//                        String actionId = result.getAction().getActionId();
////                        String type = result.getAction().getType(); // "ActionTaken" | "Opened"
//
//                        String title = result.getNotification().getTitle();
//                        CoexclLogs.errorLog("Notification", "title"+ title);
//                        CoexclLogs.errorLog("Notification", "title"+ result.getNotification().getGroupMessage());
//                    }
//                });
    }

    public static Context getContext() {
        return CONTEXT;
    }

//    @Override
//    public void onTerminate() {
//        super.onTerminate();
////        LocalBroadcastManager.getInstance(CONTEXT).unregisterReceiver(lReceiver);
//    }

//    BroadcastReceiver lReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if(intent == null && intent.getExtras() == null)
//                return;
//
//            Event lName = Freshchat.getEventFromBundle(intent.getExtras());
//            CoexclLogs.errorLog("TAG", "FreshChat lName - "+lName);
//        }
//    };
}
