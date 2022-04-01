package com.home.coexcleducation.ui.registration

import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.reset_password_layout.*
import java.io.StringWriter
import java.io.Writer
import java.util.HashMap

class ResetPassword : AppCompatActivity() {

    lateinit var dialog : Dialog
    var TAG = "ResetPassword"
    var mMobileNumber = ""
    var mPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password_layout)
        ViewUtils().setWindowBackground(this)
        mMobileNumber = intent.getStringExtra("mobile")!!
        name.text = "Reset Password"


        back_close.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        changePassword.setOnClickListener{
            mPassword = password.text.toString()
            var mConfirmPassword = confirm_password.text.toString()

            if(mMobileNumber != "" && mPassword != "" && mConfirmPassword != "") {
                if(mPassword.length > 5 && mConfirmPassword.length > 5) {
                    if(mPassword == mConfirmPassword) {
                        password.error = null
                        confirm_password.error = null
                        ChangePasswordTask().execute()
                    } else {
                        password.error = "Password mismatched"
                        confirm_password.error = "Password mismatched"
                        ViewUtils().displayToast("Password mismatched!", "failure", this@ResetPassword, "")
                    }
                } else {
                    password.error = "Password should be min 6 digits"
                    confirm_password.error = "Password should be min 6 digits"
                    ViewUtils().displayToast("Password should be min 6 digits", "failure", this@ResetPassword, "")
                }
            } else {
                ViewUtils().displayToast("Please fill mandatory fields!", "failure", this@ResetPassword, "")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }

    fun constructParams(): String {
        try {
            val lWriter: Writer = StringWriter()

            val mReqMap: HashMap<String, Any> = hashMapOf()
            mReqMap["mobile"] = mMobileNumber
            mReqMap["password"] = mPassword
            ObjectMapper().writeValue(lWriter, mReqMap)
            return lWriter.toString()
        } catch (e: java.lang.Exception) {
            CoexclLogs.printException(e)
        }
        return ""
    }


    inner class ChangePasswordTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ViewUtils().displayProgressDialog("Please wait..", this@ResetPassword)
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = HttpHelper()
                lHttpHelper.payload = constructParams()
                lHttpHelper = SignUpUtils().forgotPasswrod(this@ResetPassword, lHttpHelper)
                CoexclLogs.errorLog(TAG, "ResetPassword req - " + lHttpHelper.url)
                CoexclLogs.errorLog(TAG, "ResetPassword response - " + lHttpHelper.response)
                return lHttpHelper.response
            } catch (e : java.lang.Exception) {
                e.printStackTrace()
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(dialog.isShowing)
                dialog.dismiss()

            try {
                if (!result.isNullOrEmpty()) {
                    var lMapper = ObjectMapper()
                    var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                    if (lResponseObject.containsKey("response") && lResponseObject["response"]!! == true) {
                         ViewUtils().displayToast("Password Changed Successfully, Please login", "success", this@ResetPassword, "")
                         Handler().postDelayed(Runnable {
                             var lIntent = Intent(this@ResetPassword, LoginActivity::class.java)
                             startActivity(lIntent)
                         }, 3000)
                    } else {
                        ViewUtils().displayToast("Failed to change Password!", "failure", this@ResetPassword, "")
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@ResetPassword, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}