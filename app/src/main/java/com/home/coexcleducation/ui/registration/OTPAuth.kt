package com.home.coexcleducation.ui.registration

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.home.coexcleducation.R
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.utils.ApiConstant
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.ViewUtils
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher
import kotlinx.android.synthetic.main.otp_layout.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


class OTPAuth : AppCompatActivity() {

    lateinit var smsVerifyCatcher : SmsVerifyCatcher
    lateinit var mMobileNumber : String
    lateinit var mSessionID : String
    lateinit var mOTP : String
    lateinit var mSendOTP : TextView
    lateinit var mVerfyOTP : TextView
    lateinit var mTimer : TextView
    lateinit var mMobileET : EditText
    lateinit var mOtpET : EditText
    lateinit var mOtpLayout : TextInputLayout
    var TAG = "OTPVerification"
    lateinit var mProgress : LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_layout)

        mProgress = findViewById(R.id.progress_bar)
        mSendOTP = findViewById(R.id.send_otp)
        mVerfyOTP = findViewById(R.id.verify_otp)
        mTimer = findViewById(R.id.timer)
        mMobileET = findViewById(R.id.mobile)
        mOtpET = findViewById(R.id.otp_et)
        mOtpLayout = findViewById(R.id.otp_layout)

        back_close.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
            finish()
        }

        smsVerifyCatcher = SmsVerifyCatcher(this) { message ->
            CoexclLogs.errorLog("OTPVerification", "OTPVerification - $message")
            val code: String = parseCode(message) //Parse verification code
            mOTP = code
            mOtpET.setText(code) //set code in edit text
            VerifyOTP().execute()
        }

        mVerfyOTP.setOnClickListener{
            VerifyOTP().execute()
        }

        mMobileET.addTextChangedListener { it: Editable? ->
            if (it!!.length == 10) {
                mobile.error = null
            }
        }

        mSendOTP.setOnClickListener{
            mMobileNumber = mMobileET.text.toString()
            if (mMobileNumber != "") {
                if (mMobileNumber.length == 10) {
                    mobile.error = null
                    RequestOTP().execute()
//                    var lIntent = Intent(this@OTPVerification, SignupActivity::class.java)
//                    lIntent.putExtra("mobile", mMobileNumber)
//                    lIntent.putExtra("action", "signup")
//                    startActivity(lIntent)
                } else {
                    mobile.error = "Invalid mobile number"
                }
            } else {
                mobile.error = "Please enter mobile number"
            }
        }

        val client = SmsRetriever.getClient(this /* context */)
        val task: Task<Void> = client.startSmsRetriever()

        task.addOnSuccessListener(OnSuccessListener<Void?> {
            CoexclLogs.errorLog(TAG, "Onsucce")
        })

        task.addOnFailureListener(OnFailureListener {
            // Failed to start retriever, inspect Exception for more details
            CoexclLogs.errorLog(TAG, "Onfailure "+it.toString())
            // ...
        })

//      smsVerifyCatcher.setPhoneNumberFilter("7903376289");

        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        applicationContext.registerReceiver(broadCastReceiver, intentFilter)
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            CoexclLogs.errorLog(TAG, "Onsuccess sms - "+intent!!.data)
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
        finish()
    }

    fun startCounter() {
        var quizDurationInMilliSec = 2 * 60000
        object : CountDownTimer(quizDurationInMilliSec.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.visibility = View.VISIBLE
                timer.setText(
                    String.format(
                        "%02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        millisUntilFinished
                                    )
                                )
                    ) + " mins"
                );
            }

            override fun onFinish() {
                timer.visibility = View.GONE
                mMobileET.isEnabled = true
                mOtpET.visibility = View.GONE
                mSendOTP.visibility = View.VISIBLE
                mTimer.visibility = View.INVISIBLE
                mVerfyOTP.visibility = View.GONE
            }
        }.start()
    }

    private fun parseCode(message: String): String {
        val p: Pattern = Pattern.compile("\\b\\d{6}\\b")
        val m: Matcher = p.matcher(message)
        var code = ""
        while (m.find()) {
            code = m.group(0)
        }
        return code
    }

    override fun onStart() {
        super.onStart()
        smsVerifyCatcher.onStart()
    }

    override fun onStop() {
        super.onStop()
        smsVerifyCatcher.onStop()
    }

    /**
     * need for Android 6 real time permissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    inner class RequestOTP : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mProgress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = HttpHelper()
                lHttpHelper.url = ApiConstant.OTP_API + mMobileNumber + "/AUTOGEN"
                lHttpHelper = SignUpUtils().sendOtp(this@OTPAuth, lHttpHelper)
                CoexclLogs.errorLog(TAG, "REQ OTP req - " + lHttpHelper.url)
                CoexclLogs.errorLog(TAG, "REQ OTP response - " + lHttpHelper.response)
                return lHttpHelper.response
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                if (!result.isNullOrEmpty()) {
                    var lMapper = ObjectMapper()
                    var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                    if (lResponseObject.containsKey("Status") && lResponseObject["Status"]!! == "Success") {
                        if(lResponseObject.containsKey("Details")) {
                            mSessionID = lResponseObject["Details"].toString()
                            mMobileET.isEnabled = false
                            mSendOTP.visibility = View.GONE
                            mVerfyOTP.visibility = View.VISIBLE
                            mOtpLayout.visibility = View.VISIBLE
                            mTimer.visibility = View.VISIBLE
                            startCounter()
                        }
                    } else {
                        ViewUtils().displayToast(
                            "Invalid Mobile/Password",
                            "failure",
                            this@OTPAuth,
                            ""
                        )
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@OTPAuth, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    inner class VerifyOTP : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mProgress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = HttpHelper()
                lHttpHelper.url = ApiConstant.VERIFY_OTP_API+mSessionID+"/"+mOTP
                lHttpHelper = SignUpUtils().sendOtp(this@OTPAuth, lHttpHelper)
                CoexclLogs.errorLog(TAG, "VERIFY OTP req - " + lHttpHelper.url)
                CoexclLogs.errorLog(TAG, "VERIFY OTP response - " + lHttpHelper.response)
                return lHttpHelper.response
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            mProgress.visibility = View.GONE

            try {
                if (!result.isNullOrEmpty()) {
                    var lMapper = ObjectMapper()
                    var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                    if (lResponseObject.containsKey("Status") && lResponseObject["Status"]!! == "Success") {
                        if(lResponseObject.containsKey("Details") && lResponseObject["Details"]!! == "OTP Matched") {
                            if(intent.getStringExtra("action").equals("signup")) {
                                var lIntent = Intent(this@OTPAuth, SignupActivity::class.java)
                                lIntent.putExtra("mobile", mMobileNumber)
                                lIntent.putExtra("action", "signup")
                                startActivity(lIntent)
                            } else {
                                var lIntent = Intent(this@OTPAuth, ResetPassword::class.java)
                                lIntent.putExtra("mobile", mMobileNumber)
                                lIntent.putExtra("action", "resetPassword")
                                startActivity(lIntent)
                            }
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
                            finishAffinity()
                        } else {
                            ViewUtils().displayToast(
                                "Incorrect OTP, Please try again",
                                "failure",
                                this@OTPAuth,
                                ""
                            )
                        }
                    } else {
                        ViewUtils().displayToast(
                            "Something went wrong",
                            "failure",
                            this@OTPAuth,
                            ""
                        )
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@OTPAuth, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}