package com.home.coexcleducation.premium

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import kotlinx.android.synthetic.main.plan_detail_activity.*


class PremiumDetailView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plan_detail_activity)

        back.setOnClickListener{
            finish()
        }

        downgrade.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/account/subscriptions"))
            startActivity(browserIntent)
        }
    }
}