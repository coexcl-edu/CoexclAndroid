package com.home.coexcleducation.ui.videos

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.ui.adaptar.VideoAdaptar
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.Helper
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.note_for_customer_layout.*
import kotlinx.android.synthetic.main.video_listing_activity.*
import kotlinx.android.synthetic.main.video_listing_activity.name
import java.util.*
import kotlin.collections.ArrayList

class VideoListingActivity : AppCompatActivity() {

    lateinit var mContext : Context
    var TAG = "VideoListingActivity"
    lateinit var mProgressView : View
    lateinit var mListOfVideos : ArrayList<HashMap<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_listing_activity)
        mContext = this
        mProgressView = findViewById(R.id.progress_bar)

        name.text = intent.getStringExtra("subject")

        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        subject_videos.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var lIntent = Intent(this, VideoPlayerYoutube::class.java)
            lIntent.putExtra("videoIndex", position)
            lIntent.putExtra("videoList", mListOfVideos)
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Video listing")
        FetchVideos().execute()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }


    fun constructRequestPayload(): String {
        var mHashMap = HashMap<String, String>()
        mHashMap.put("subject", intent.getStringExtra("subject")!!)
        mHashMap.put("class", UserDetails.getInstance().className)
        return Helper().constructJson(mHashMap)
    }

    inner class FetchVideos : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mProgressView.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = VideoUtils().fetchVideo(mContext, constructRequestPayload())
                CoexclLogs.errorLog(TAG, "Req from Notice - " + lHttpHelper!!.payload)
                CoexclLogs.errorLog(TAG, "Req URL from Notice - " + lHttpHelper!!.url)
                CoexclLogs.errorLog(TAG, "Response from Notice - " + lHttpHelper.response)
                return lHttpHelper.response
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            mProgressView.visibility = View.GONE

            try {
                if (!result.isNullOrEmpty()) {
                    var lMapper = ObjectMapper()
                    var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                    if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                        var data = lResponseObject.get("data") as HashMap<String, Any>
                        if (data.containsKey("links")) {
                            mListOfVideos = data.get("links") as ArrayList<HashMap<String, String>>
                            if(mListOfVideos.size > 0) {
                                var adapter = VideoAdaptar(this@VideoListingActivity, mListOfVideos, "video")
                                subject_videos.adapter = adapter
                                activity_error.visibility = View.GONE
                            } else {
                                activity_error.visibility = View.VISIBLE
                            }
                        } else {
                            ViewUtils().displayToast("Something went wrong", "failure", this@VideoListingActivity, "")
                            activity_error.visibility = View.VISIBLE
                        }
                    } else {
                        activity_error.visibility = View.VISIBLE
                        ViewUtils().displayToast("Something went wrong", "failure", this@VideoListingActivity, "")
                    }
                } else {
                    activity_error.visibility = View.VISIBLE
                    ViewUtils().displayToast("Something went wrong", "failure", this@VideoListingActivity, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ViewUtils().displayToast("Something went wrong", "failure", this@VideoListingActivity, "")
                activity_error.visibility = View.VISIBLE
            }
        }

    }

}