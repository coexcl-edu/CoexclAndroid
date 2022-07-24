package com.home.coexcleducation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.home.coexcleducation.intercom.IntercomHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.ui.registration.SignUpUtils
import com.home.coexcleducation.utils.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var navBar : BottomNavigationView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        navBar = findViewById<BottomNavigationView>(R.id.nav_view)


        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener {
                controller, destination, arguments ->
            CoexclLogs.errorLog("TAG", "destination - "+destination.label)
            if (destination.label == "Home") {
                chatIcon.visibility = View.GONE
            } else if(destination.label == "Notifications") {
                chatIcon.visibility = View.VISIBLE
                PreferenceHelper.getSharedPreference(this).edit().putInt(PreferenceHelper.NOTIFICATION_COUNT, 0).commit()
                updateNotificationCount()
            } else {
                chatIcon.visibility = View.VISIBLE
            }

        })


        chatIcon.setOnClickListener{
            if (!UserDetails.getInstance().subscribed) {
                if (UserDetails.getInstance().sessionCount == 0) {
                    ViewUtils().displayStrongScreenForPremium(this)
                } else {
                    IntercomHelper().startIntercomChat(this);
                    UpdateSession().execute()
                }
            } else {
                IntercomHelper().startIntercomChat(this);
            }

        }

        if(UserDetails.getInstance().fcmToken == "") {
            FCMTokenUpdate().execute(this)
        }

//        if(!UserDetails.getInstance().subscribed)
            GetProfile().execute()
        updateNotificationCount()
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadCastReceiver, IntentFilter("com.coexcl.notification"))
    }

    private fun updateNotificationCount() {
        val count = PreferenceHelper.getSharedPreference(MainApplication.CONTEXT)
            .getInt(PreferenceHelper.NOTIFICATION_COUNT, 0)
        if(count > 0) {
            navBar.getOrCreateBadge(R.id.navigation_notifications).number = count
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadCastReceiver)
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            CoexclLogs.errorLog("TAG", "Onsuccess sms - "+intent!!.data)
            updateNotificationCount()
        }
    }

    inner class FCMTokenUpdate : AsyncTask<Context, String, String>() {

        override fun doInBackground(vararg params: Context): String {
            return try {
                Utilty().updateToken(params[0], FirebaseInstanceId.getInstance().getToken(Constants.FCM_REGISTRATION_KEY, "FCM")!!).payload
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }

    inner class GetProfile : AsyncTask<Context, String, String>() {

        override fun doInBackground(vararg params: Context): String {
            try {
                var response = SignUpUtils().getProfile(this@MainActivity, UserDetails.getInstance().id).response
                CoexclLogs.errorLog("TAG", "get Profile Response - $response")
                Utilty().insertLoginData(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

    }

    inner class UpdateSession : AsyncTask<Context, String, String>() {

        override fun doInBackground(vararg params: Context): String {
            Utilty().updateSessionCount(this@MainActivity)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }
    }



}