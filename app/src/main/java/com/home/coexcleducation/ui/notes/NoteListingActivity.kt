package com.home.coexcleducation.ui.notes

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.listner.NotesDeletionListner
import com.home.coexcleducation.ui.adaptar.NotesAdaptar
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.PreferenceHelper
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.note_listing.*
import kotlinx.android.synthetic.main.note_listing.header_background
import kotlinx.android.synthetic.main.signup_layout.*
import java.io.StringWriter
import java.io.Writer
import java.util.*


class NoteListingActivity : AppCompatActivity(), NotesDeletionListner {

    lateinit var mProgressView : View
    var TAG = "NoteListingActivity"
    lateinit var mPreferences: SharedPreferences
    lateinit var mListOfNotes : List<HashMap<String, String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_listing)
        mProgressView = findViewById(R.id.progress_bar)
        mPreferences = PreferenceHelper.getSharedPreference(this)

        if (applicationContext.packageName == "com.home.coexcleducation") {
            header_background.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.one))
        } else {
            header_background.alpha = 0.5f
            header_background.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cofi_header_back))
        }

        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
            finish()
        }

        notes_listing.onItemClickListener = OnItemClickListener { parent, view, position, id ->

            val lIntent = Intent(this@NoteListingActivity, NotesDetailViewActivity::class.java)
            lIntent.putExtra("title", mListOfNotes.get(position).get("title"))
            lIntent.putExtra("desc", mListOfNotes.get(position).get("description"))
            lIntent.putExtra("id", mListOfNotes.get(position).get("_id"))
            lIntent.putExtra("newNote", false)
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        editnotes.setOnClickListener{
            var intent = Intent(this, EditNotesActivity::class.java)
            intent.putExtra("newNote", true)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

    }

    override fun onResume() {
        super.onResume()
        FetchNotes().execute()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
        finish()
    }


    fun constructParams(): String {
        try {
            val lWriter: Writer = StringWriter()

            val mReqMap: HashMap<String, Any> = hashMapOf()
            mReqMap["userId"] = UserDetails.getInstance().id
            ObjectMapper().writeValue(lWriter, mReqMap)
            return lWriter.toString()
        } catch (e: java.lang.Exception) {
            CoexclLogs.printException(e)
        }
        return ""
    }

    inner class FetchNotes : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mProgressView.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = NotesUtility().fetchNotes(this@NoteListingActivity, constructParams())
                CoexclLogs.errorLog(TAG, "Req from Notes - " + lHttpHelper!!.payload)
                CoexclLogs.errorLog(TAG, "Req URL from Notes - " + lHttpHelper!!.url)
                CoexclLogs.errorLog(TAG, "Response from Notes - " + lHttpHelper.response)
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
                        if (data.containsKey("notes")) {
                            mListOfNotes = data.get("notes") as List<HashMap<String, String>>
                            if(mListOfNotes.isNotEmpty()) {
                                var adapter = NotesAdaptar(this@NoteListingActivity, mListOfNotes)
                                notes_listing.adapter = adapter
                                adapter.setOnclickListner(this@NoteListingActivity)
                                notes_error.visibility = View.GONE
                                notes_listing.visibility =  View.VISIBLE
                            } else {
                                notes_listing.visibility =  View.GONE
                                notes_error.visibility = View.VISIBLE
                            }
                        } else {
                            notes_error.visibility = View.VISIBLE
                            ViewUtils().displayToast("Something went wrong", "failure", this@NoteListingActivity, "")
                        }
                    } else {
                        notes_error.visibility = View.VISIBLE
                        ViewUtils().displayToast("Something went wrong", "failure", this@NoteListingActivity, "")
                    }
                } else {
                    notes_error.visibility = View.VISIBLE
                    ViewUtils().displayToast("Something went wrong", "failure", this@NoteListingActivity, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun deleteNoes(position: Int) {
        DeleteNotes().execute(mListOfNotes.get(position).get("_id"))
    }

    inner class DeleteNotes : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mProgressView.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String): String {
            try {
                var lHttpHelper = NotesUtility().deleteNotes(this@NoteListingActivity, params[0])
                CoexclLogs.errorLog(TAG, "Req URL from Notes - " + lHttpHelper!!.url)
                CoexclLogs.errorLog(TAG, "Response from Notes - " + lHttpHelper.response)
                return lHttpHelper.response
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            FetchNotes().execute()
        }

    }
}