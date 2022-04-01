package com.home.coexcleducation.ui.summary

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.ui.adaptar.VideoAdaptar
import com.home.coexcleducation.ui.videos.VideoPlayerYoutube
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.video_listing_activity.*

class SummaryListingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_listing_activity)

        name.text = intent.getStringExtra("subject")

        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        subject_videos.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var lIntent = Intent(this, SummaryDetailActivity::class.java)
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
        FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Summary listing")
//        setupWindowAnimations()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }

//    private fun setupWindowAnimations() {
//        val fade = Fade()
//        fade.setDuration(1000)
//        window.enterTransition = fade
//        val slide = Slide()
//        slide.setDuration(1000)
//        window.returnTransition = slide
//    }
}