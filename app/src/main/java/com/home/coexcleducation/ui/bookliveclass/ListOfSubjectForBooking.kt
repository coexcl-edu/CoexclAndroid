package com.home.coexcleducation.ui.bookliveclass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.listofliveclassbooking.*

class ListOfSubjectForBooking : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listofliveclassbooking)

        maths.setOnClickListener{
            var lIntent = Intent(this, ListOfLiveClassForBooking::class.java)
            lIntent.putExtra("subject", "maths")
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Book Live Class - Maths")
        }

        physics.setOnClickListener{
            var lIntent = Intent(this, ListOfLiveClassForBooking::class.java)
            lIntent.putExtra("subject", "physics")
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Book Live Class - Physics")
        }

        chemistry.setOnClickListener{
            var lIntent = Intent(this, ListOfLiveClassForBooking::class.java)
            lIntent.putExtra("subject", "chemistry")
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Book Live Class- Chemistry")
        }

        biology.setOnClickListener{
            var lIntent = Intent(this, ListOfLiveClassForBooking::class.java)
            lIntent.putExtra("subject", "biology")
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Book Live Class- Biology")
        }

        back.setOnClickListener{
           exit()
        }
    }

    fun exit() {
        finish()
        ViewUtils().exitActivityToRight(this)
    }

    override fun onBackPressed() {
        exit()
    }
}