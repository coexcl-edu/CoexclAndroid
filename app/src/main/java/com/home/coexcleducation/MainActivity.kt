package com.home.coexcleducation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.freshchat.consumer.sdk.ConversationOptions
import com.freshchat.consumer.sdk.Freshchat
import com.freshchat.consumer.sdk.FreshchatConfig
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.home.coexcleducation.intercom.IntercomHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.PreferenceHelper
import com.home.coexcleducation.utils.Utilty
import io.intercom.android.sdk.Intercom
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

//        val config = FreshchatConfig(resources.getString(R.string.chat_app_id), resources.getString(R.string.chat_app_key))
//        config.domain = "msdk.in.freshchat.com"
//        config.isCameraCaptureEnabled = true;
//        config.isGallerySelectionEnabled = true;
//        config.isResponseExpectationEnabled = true;
//        Freshchat.getInstance(applicationContext).init(config)
//
//
//        val freshchatUser = Freshchat.getInstance(applicationContext).user
//        var lUserDetails = UserDetails.getInstance()
//        freshchatUser.firstName = lUserDetails.name
//        freshchatUser.email = lUserDetails.email
//        freshchatUser.setPhone("+91", lUserDetails.mobile)
//
//        Freshchat.getInstance(applicationContext).user = freshchatUser
//
//        val userMeta: MutableMap<String, String> = HashMap()
//        userMeta["schoolName"] = lUserDetails.schoolName
//        userMeta["schoolCode"] = lUserDetails.schoolCode
//        userMeta["city"] = lUserDetails.city
//        userMeta["class"] = lUserDetails.className
//        userMeta["userType"] = if (lUserDetails.subscribed) "Premium" else "Free"
//        userMeta["fatherName"] = lUserDetails.fatherName
//
//        Freshchat.getInstance(applicationContext).setUserProperties(userMeta)
//
//
//        val tags: MutableList<String> = ArrayList()
//        tags.add("order_queries")
//        val options = ConversationOptions()
//                .filterByTags(tags, "Order Queries")

        chatIcon.setOnClickListener{
//            Freshchat.showConversations(applicationContext, options);
            IntercomHelper().startIntercomChat(this);
        }

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
}