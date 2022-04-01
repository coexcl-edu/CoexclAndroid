package com.home.coexcleducation.ui.notes

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.Helper
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.note_for_customer_layout.*
import kotlinx.android.synthetic.main.note_listing.*
import org.apache.commons.text.StringEscapeUtils

/**
 * Abhishek
 * created on 22 apr 2020
 */

class EditNotesActivity : AppCompatActivity() {

    lateinit var mContext : Context
    var TAG : String = "NotesForCustomer"
    var marginOfError = 0
    var maxLength = 480
    lateinit var mProgressDialog : Dialog
    lateinit var mSave : TextView
    var mNotesOnPageLoad = ""
    var mNotesTitle = ""
    var mNewNote = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_for_customer_layout)
        mContext = this
        marginOfError = Math.round(Helper.convertDpToPixel(50f, mContext))
        mSave = findViewById(R.id.save)

        mNewNote = intent.getBooleanExtra("newNote", false)

        mSave.setOnClickListener{
            if(mSave.tag.equals(true)) {
                UpdateNotes().execute()
            }
        }

        close.setOnClickListener{
            exitActivity()
        }

        val listener = ViewTreeObserver.OnGlobalLayoutListener { isKeyboardVisible() }
        parentlayout.viewTreeObserver.addOnGlobalLayoutListener(listener)

        notes_for_customer.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                lengthremaining.text = "Characters left : "+(maxLength - (s.toString().length))
                enableSaveButton()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        note_title.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                enableSaveButton()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        renderData()
    }

    fun isKeyboardVisible(): Boolean {
        val visibleHeight = Rect()
        findViewById<View>(android.R.id.content).getWindowVisibleDisplayFrame(visibleHeight)
        val isVisible: Boolean = findViewById<View>(android.R.id.content).height - visibleHeight.height() > marginOfError
        if (isVisible) findViewById<View>(android.R.id.content).setPadding(0, 0, 0, findViewById<View>(android.R.id.content).height - (visibleHeight.height() + marginOfError/2)) else findViewById<View>(android.R.id.content).setPadding(0, 0, 0, 0)
        return isVisible
    }

    fun exitActivity() {
        if(mSave.tag.equals(true)) {
             displayAlertDialogOnQuiting("Notes not yet saved, Want to quit your changes?", "No", "Yes")
        } else {
            ViewUtils().exitActivityToRight(this as Activity)
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        CoexclLogs.infoLog(javaClass.name, "onKeyDown")
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitActivity()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    fun constructRequestPayload(): String {
        var mHashMap = HashMap<String, String>()
        if(!mNewNote) {
            mHashMap.put("noteId", intent.getStringExtra("id").toString())
        } else {
            mHashMap.put("userId", UserDetails.getInstance().id)
        }
        mHashMap.put("title", StringEscapeUtils.escapeHtml4(note_title.text.toString()))
        mHashMap.put("description", StringEscapeUtils.escapeHtml4(notes_for_customer.text.toString()))
        return Helper().constructJson(mHashMap)
    }

    private fun renderData() {
        if(!mNewNote) {
            mNotesOnPageLoad = intent.getStringExtra("desc").toString()
            mNotesTitle = intent.getStringExtra("title").toString()
        }
        note_title.setText(StringEscapeUtils.unescapeHtml4(mNotesTitle))
        notes_for_customer.setText(StringEscapeUtils.unescapeHtml4(mNotesOnPageLoad))
        notes_for_customer.requestFocus()
        lengthremaining.text = "Characters left : "+(maxLength - (notes_for_customer.text.toString().length))
        mSave.setTextColor( ContextCompat.getColor(mContext, R.color.colorAccent))
        mSave.tag = false
    }

     fun enableSaveButton() {

         if(notes_for_customer.text.toString() != mNotesTitle && notes_for_customer.text.toString() != "" && note_title.text.toString() != mNotesOnPageLoad && note_title.text.toString() != "") {
            CoexclLogs.errorLog("NotesForCustomer ", "comming in true -"+mNotesOnPageLoad)
            mSave.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            mSave.tag = true
        } else {
            CoexclLogs.errorLog("NotesForCustomer ", "comming in nops -"+mNotesOnPageLoad)
            mSave.setTextColor( ContextCompat.getColor(mContext, R.color.colorAccent))
            mSave.tag = false
        }
    }

    fun displayLoader(show: Boolean, msg: String) {
        if(show) {
            mProgressDialog = ViewUtils().displayProgressDialog(msg, mContext)
        } else {
            mProgressDialog.dismiss()
        }
    }



    private fun displayAlertDialogOnQuiting(alertmessage: String, leftbtntitle: String, rightbtntitle: String) {
        try {
            val dialog = Dialog(mContext, R.style.DialogCustomTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_popup)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val window = dialog.window
            window!!.setGravity(Gravity.CENTER)
            window.setLayout((getWindow().peekDecorView().width * 0.85).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            val lAlterMessage = dialog.findViewById<TextView>(R.id.Alert_Message)
            val leftBtnText = dialog.findViewById<TextView>(R.id.Dialog_LeftBtn)
            val rightBtnText = dialog.findViewById<TextView>(R.id.Dialog_RightBtn)
            val msg = dialog.findViewById<TextView>(R.id.Alert_Title)
            val lYesButton = dialog.findViewById<LinearLayout>(R.id.ConfirmLayout)
            val lNoButton = dialog.findViewById<LinearLayout>(R.id.CancelLayout)
            lAlterMessage.text = alertmessage
            leftBtnText.text = "Cancel"
            rightBtnText.text = "Ok"
            msg.text = "Save"
            lNoButton.setOnClickListener {
                dialog.dismiss()

            }
            lYesButton.setOnClickListener {
                dialog.dismiss()
                ViewUtils().exitActivityToRight(this)
                finish()
            }
            dialog.show()
        } catch (e: Exception) {
            CoexclLogs.printException(e);
        }
    }

    inner class UpdateNotes : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            displayLoader( true, "Updating Note..")
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = HttpHelper()
                if(mNewNote) {
                    lHttpHelper = NotesUtility().addNotes(this@EditNotesActivity, constructRequestPayload())!!
                } else {
                    lHttpHelper = NotesUtility().updateNotes(this@EditNotesActivity, constructRequestPayload())!!
                }
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
            displayLoader( false, "Updating Note..")

            try {
                if (!result.isNullOrEmpty()) {
                    var lMapper = ObjectMapper()
                    var lResponseObject = lMapper.readValue(result, java.util.HashMap::class.java)
                    if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
//                        var data = lResponseObject.get("data") as java.util.HashMap<String, Any>
                        ViewUtils().displayToast("Sweet! Note Updated.", "success", this@EditNotesActivity, "edit")
                    } else {
                        ViewUtils().displayToast("Something went wrong", "failure", this@EditNotesActivity, "")
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@EditNotesActivity, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}