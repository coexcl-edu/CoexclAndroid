package com.home.coexcleducation.ui.lession

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.ui.ComingSoonActivty
import com.home.coexcleducation.ui.summary.SummaryListingActivity
import com.home.coexcleducation.ui.videos.VideoListingActivity
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.subject_details_layout.*
import kotlinx.android.synthetic.main.subject_details_layout.name
import kotlinx.android.synthetic.main.video_listing_activity.back

class SubjectDetailActivtiy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subject_details_layout)

        name.text = intent.getStringExtra("subject")

        back.setOnClickListener{
           ViewUtils().exitActivityToRight(this)
        }

        videos.setOnClickListener{
           var lIntent = Intent(this, VideoListingActivity::class.java)
            lIntent.putExtra("subject", intent.getStringExtra("subject"))
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        quiz.setOnClickListener{
//            var lIntent = Intent(this, QuizActivity::class.java)
            var lIntent = Intent(this, ComingSoonActivty::class.java)
            lIntent.putExtra("subject", intent.getStringExtra("subject"))
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        summary.setOnClickListener{
//            var lIntent = Intent(this, SummaryListingActivity::class.java)
            var lIntent = Intent(this, ComingSoonActivty::class.java)
            lIntent.putExtra("subject", intent.getStringExtra("subject"))
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@SubjectDetailActivtiy, "", "Home", "Subject Detail")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }
}