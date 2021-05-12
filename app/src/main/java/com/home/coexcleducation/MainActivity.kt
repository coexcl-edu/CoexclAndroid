package com.home.coexcleducation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.freshchat.consumer.sdk.ConversationOptions
import com.freshchat.consumer.sdk.Freshchat
import com.freshchat.consumer.sdk.FreshchatConfig
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)




        val config = FreshchatConfig(resources.getString(R.string.chat_app_id), resources.getString(R.string.chat_app_key))
        config.domain = "msdk.in.freshchat.com"
        config.isCameraCaptureEnabled = true;
        config.isGallerySelectionEnabled = true;
        config.isResponseExpectationEnabled = true;
        Freshchat.getInstance(applicationContext).init(config)

        val tags: MutableList<String> = ArrayList()
        tags.add("order_queries")
        val options = ConversationOptions()
                .filterByTags(tags, "Order Queries")

        chatIcon.setOnClickListener{
            Freshchat.showConversations(applicationContext, options);
        }

    }
}