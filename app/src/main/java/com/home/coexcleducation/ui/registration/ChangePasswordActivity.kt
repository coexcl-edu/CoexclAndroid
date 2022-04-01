package com.home.coexcleducation.ui.registration

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.change_password_layout.*
import kotlinx.android.synthetic.main.change_password_layout.back_close
import kotlinx.android.synthetic.main.change_password_layout.changePassword
import kotlinx.android.synthetic.main.change_password_layout.confirm_password
import kotlinx.android.synthetic.main.change_password_layout.name
import kotlinx.android.synthetic.main.change_password_layout.password
import kotlinx.android.synthetic.main.reset_password_layout.*
import java.io.StringWriter
import java.io.Writer
import java.util.HashMap

class ChangePasswordActivity : AppCompatActivity() {

    lateinit var dialog : Dialog
    var TAG = "ChangePasswordActivity"
    var mMobileNumber = ""
    var mPassword = ""
    var mOldPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password_layout)
        ViewUtils().setWindowBackground(this)
        mMobileNumber = UserDetails.getInstance().mobile
        name.text = "Change Password"

        back_close.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        changePassword.setOnClickListener{
            mOldPassword = old_password.text.toString()
            mPassword = password.text.toString()
            var mConfirmPassword = confirm_password.text.toString()

            if(mMobileNumber != "" && mPassword != "" && mConfirmPassword != "" && mOldPassword != null) {
                if(mPassword.length > 5 && mConfirmPassword.length > 5) {
                    if(mPassword == mConfirmPassword) {
                        password.error = null
                        confirm_password.error = null
                        ChangePasswordTask().execute()
                    } else {
                        password.error = "Password mismatched"
                        confirm_password.error = "Password mismatched"
                        ViewUtils().displayToast("Password mismatched!", "failure", this@ChangePasswordActivity, "")
                    }
                } else {
                    password.error = "Password should be min 6 digits"
                    confirm_password.error = "Password should be min 6 digits"
                    ViewUtils().displayToast("Password should be min 6 digits", "failure", this@ChangePasswordActivity, "")
                }
            } else {
                ViewUtils().displayToast("Please fill mandatory fields!", "failure", this@ChangePasswordActivity, "")
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
            mReqMap["newpassword"] = mPassword
            mReqMap["oldpassword"] = mOldPassword
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
            dialog = ViewUtils().displayProgressDialog("Please wait..", this@ChangePasswordActivity)
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = HttpHelper()
                lHttpHelper.payload = constructParams()
                lHttpHelper = SignUpUtils().changePassword(this@ChangePasswordActivity, lHttpHelper)
                CoexclLogs.errorLog(TAG, "ChangePasswordTask req - " + lHttpHelper.url)
                CoexclLogs.errorLog(TAG, "ChangePasswordTask response - " + lHttpHelper.response)
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
                            ViewUtils().displayToast("Password Changed Successfully", "success", this@ChangePasswordActivity, "edit")
                        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@ChangePasswordActivity, "", "Home", "Change password success")
                    } else {
                        ViewUtils().displayToast("Failed to change Password!", "failure", this@ChangePasswordActivity, "")
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@ChangePasswordActivity, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}