package com.home.coexcleducation.ui.registration

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.JobIntentService
import androidx.core.content.ContextCompat
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.MainActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.fcm.RegistrationIntentService
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.*
import kotlinx.android.synthetic.main.login_activity.*
import java.io.StringWriter
import java.io.Writer
import java.util.*

class LoginActivity : AppCompatActivity() {

    lateinit var mMobile : String
    lateinit var mPassword : String
    var TAG = "LoginActivity"
    lateinit var mContext: Context
    lateinit var mPreferences: SharedPreferences
    lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        mContext = this
        mPreferences = PreferenceHelper.getSharedPreference(mContext)

        login.setOnClickListener{
            mMobile = username.text.toString()
            mPassword = password.text.toString()

            if(!mMobile.isNullOrEmpty() && !mPassword.isNullOrEmpty()) {
                username.error = null
                password.error = null
                LoginTask().execute()
            } else {
                username.error = "Please provide valid mobile number"
                password.error = "Please enter your password"
            }
        }

        create_account.setOnClickListener{
            var lIntent = Intent(this, OTPVerification::class.java)
            lIntent.putExtra("action", "signup")
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        forgetPassword.setOnClickListener{
            var lIntent = Intent(this, OTPVerification::class.java)
            lIntent.putExtra("action", "resetPassword")
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Forgot password")
        }

        ViewUtils().moveViewToUp(out_login_layout, 500)
    }

    fun constructParamsForSignUp(): String {
        try {
            val lWriter: Writer = StringWriter()

            val mReqMap: HashMap<String, Any> = hashMapOf()
            mReqMap["mobile"] = mMobile
            mReqMap["password"] = mPassword
            ObjectMapper().writeValue(lWriter, mReqMap)
            return lWriter.toString()
        } catch (e: java.lang.Exception) {
            CoexclLogs.printException(e)
        }
        return ""
    }

    inner class LoginTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ViewUtils().displayProgressDialog("Logging in...", this@LoginActivity)
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = HttpHelper()
                lHttpHelper.payload = constructParamsForSignUp()
                lHttpHelper = SignUpUtils().login(this@LoginActivity, lHttpHelper)
                CoexclLogs.errorLog(TAG, "Req from Signup - " + lHttpHelper.payload)
                CoexclLogs.errorLog(TAG, "Response from Signup - " + lHttpHelper.response)
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
                    if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                            Utilty().insertLoginData(result)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
                            finishAffinity()
                            JobIntentService.enqueueWork(mContext, RegistrationIntentService::class.java, ApiConstant.JOB_INTENT_REGISTRATION_ID, Intent())
                        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@LoginActivity, "", "Home", "Login Success")
                    } else {
                        ViewUtils().displayToast(lResponseObject.get("data").toString(), "failure", this@LoginActivity, "")
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@LoginActivity, "")
                    FirebaseAnalyticsCoexcl().logFirebaseEvent(this@LoginActivity, "", "Home", "Login Failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}