package com.home.coexcleducation.ui.liveclass

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.live_class_activity.*
import java.text.SimpleDateFormat
import java.util.*


class LiveClassesActivity : AppCompatActivity() {

    var TAG = "LiveClassesActivity"
    lateinit var mContext: Context
    lateinit var mClassObject : HashMap<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_class_activity)
        mContext = this

        ViewUtils().setWindowBackground(this)

        mClassObject = intent.getSerializableExtra("liveClass") as HashMap<String, Any>

        back_button.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        joinLiveClass.setOnClickListener{
            var meetUrl = mClassObject.get("videoMeetUrl").toString()
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(meetUrl))
            startActivity(browserIntent)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(this@LiveClassesActivity, "", "Home", "Live Class Zoom joined")
        }
        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@LiveClassesActivity, "", "Home", "Live Class View")
        setValue()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }

    private fun setValue() {
        try {
            val lLongValue: Long = mClassObject.get("startTime").toString().toLong();
            time.text = SimpleDateFormat("dd MMM", Locale.ENGLISH).format(Date(lLongValue))
            date.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(lLongValue))
            duration.text = mClassObject.get("duration").toString() + " mins"
            teacher.text = mClassObject.get("teacherName").toString()
            notice_body.text = mClassObject.get("description").toString()
            topicName.text = "Topic : "+mClassObject.get("topic")
            subject.text = "Subject : "+mClassObject.get("subject")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}