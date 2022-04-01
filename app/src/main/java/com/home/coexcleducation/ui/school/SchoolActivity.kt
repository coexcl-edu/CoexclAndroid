package com.home.coexcleducation.ui.school

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.ui.adaptar.ActivityAdaptar
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.school_activity.*
import java.util.HashMap

class SchoolActivity : AppCompatActivity() {

    var TAG = "SchoolActivity"
    lateinit var mProgressView : View
    lateinit var mListOfNotice : ArrayList<HashMap<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.school_activity)
        ViewUtils().setWindowBackground(this)
        mProgressView = findViewById(R.id.progress_bar)

        back_button.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        school_activity_list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, SchoolNoticeDetails::class.java)
            intent.putExtra("notice", mListOfNotice[position])
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        FetchActivities().execute()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }

    inner class FetchActivities : AsyncTask<String, String, String>() {

    override fun onPreExecute() {
        super.onPreExecute()
        mProgressView.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg params: String?): String {
        try {
            var lHttpHelper = SchoolUtils().fetchActivity(this@SchoolActivity)
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
                    if (data.containsKey("notices")) {
                        mListOfNotice = data.get("notices") as ArrayList<HashMap<String, String>>
                        if(!mListOfNotice.isNullOrEmpty()) {
                            var adapter = ActivityAdaptar(this@SchoolActivity, mListOfNotice)
                            school_activity_list.adapter = adapter
                            activity_error.visibility = View.GONE
                        } else {
                            activity_error.visibility = View.VISIBLE
                        }
                    } else {
                        activity_error.visibility = View.VISIBLE
                        ViewUtils().displayToast("Something went wrong", "failure", this@SchoolActivity, "")
                    }
                } else {
                    activity_error.visibility = View.VISIBLE
                    ViewUtils().displayToast("Something went wrong", "failure", this@SchoolActivity, "")
                }
            } else {
                activity_error.visibility = View.VISIBLE
                ViewUtils().displayToast("Something went wrong", "failure", this@SchoolActivity, "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            activity_error.visibility = View.VISIBLE
        }
    }

}
}