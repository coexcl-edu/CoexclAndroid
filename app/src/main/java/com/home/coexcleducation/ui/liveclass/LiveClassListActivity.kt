package com.home.coexcleducation.ui.liveclass

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.ui.adaptar.LiveClassAdaptar
import com.home.coexcleducation.ui.school.SchoolUtils
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.live_class_activity.*
import kotlinx.android.synthetic.main.live_class_list_activity.*
import kotlinx.android.synthetic.main.video_listing_activity.*
import kotlinx.android.synthetic.main.video_listing_activity.activity_error
import kotlinx.android.synthetic.main.video_listing_activity.back
import java.io.StringWriter
import java.io.Writer
import java.util.HashMap

class LiveClassListActivity : AppCompatActivity() {

    var TAG = "LiveClassListActivity"
    lateinit var mProgressView : View
    lateinit var mListOfClasses : ArrayList<HashMap<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_class_list_activity)
        mProgressView = findViewById(R.id.progress_bar)
        ViewUtils().setWindowBackground(this)

        if (applicationContext.packageName == "com.home.coexcleducation") {
            header_background.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.one))
        } else {
            header_background.alpha = 0.5f
            header_background.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cofi_back_three))
        }


        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        live_class.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, LiveClassesActivity::class.java)
            intent.putExtra("liveClass", mListOfClasses[position])
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@LiveClassListActivity, "", "Home", "Live Class List View")
        FetchClasses().execute()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }

    fun constructParams(): String {
        try {
            val lWriter: Writer = StringWriter()

            val mReqMap: HashMap<String, Any> = hashMapOf()
            mReqMap["schoolCode"] = UserDetails.getInstance().schoolCode
            mReqMap["class"] = UserDetails.getInstance().className
            ObjectMapper().writeValue(lWriter, mReqMap)
            return lWriter.toString()
        } catch (e: java.lang.Exception) {
            CoexclLogs.printException(e)
        }
        return ""
    }

    inner class FetchClasses : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mProgressView.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = SchoolUtils().fetchLiveClass(this@LiveClassListActivity, constructParams())
                CoexclLogs.errorLog(TAG, "Req from Live class - " + lHttpHelper!!.payload)
                CoexclLogs.errorLog(TAG, "Req URL Live class - " + lHttpHelper!!.url)
                CoexclLogs.errorLog(TAG, "Response Live class - " + lHttpHelper.response)
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
                        if (data.containsKey("liveclass")) {
                            mListOfClasses = data["liveclass"] as ArrayList<HashMap<String, Any>>
                            if(!mListOfClasses.isNullOrEmpty()) {
                                var adapter = LiveClassAdaptar(this@LiveClassListActivity, mListOfClasses)
                                live_class.adapter = adapter
                                activity_error.visibility = View.GONE
                            } else {
                                activity_error.visibility = View.VISIBLE
                            }
                        } else {
                            activity_error.visibility = View.VISIBLE
                            ViewUtils().displayToast("Something went wrong", "failure", this@LiveClassListActivity, "")
                        }
                    } else {
                        activity_error.visibility = View.VISIBLE
                        ViewUtils().displayToast("Something went wrong", "failure", this@LiveClassListActivity, "")
                    }
                } else {
                    activity_error.visibility = View.VISIBLE
                    ViewUtils().displayToast("Something went wrong", "failure", this@LiveClassListActivity, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                activity_error.visibility = View.VISIBLE
            }
        }

    }
}