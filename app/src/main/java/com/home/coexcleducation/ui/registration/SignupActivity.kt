package com.home.coexcleducation.ui.registration

import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.JobIntentService
import androidx.core.content.ContextCompat
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.home.coexcleducation.MainActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.fcm.RegistrationIntentService
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.utils.*
import io.intercom.android.sdk.Intercom
import kotlinx.android.synthetic.main.bottom_sheet_signup.view.*
import kotlinx.android.synthetic.main.profile_update_view.*
import kotlinx.android.synthetic.main.signup_layout.*
import kotlinx.android.synthetic.main.signup_layout.class_spinner
import kotlinx.android.synthetic.main.signup_layout.name
import java.io.StringWriter
import java.io.Writer


class SignupActivity : AppCompatActivity() {

    var TAG = "SignupActivity"
    lateinit var mName : String
    lateinit var mMobile : String
    lateinit var mPassword : String
    lateinit var mConfirmPassword : String
    lateinit var mSchoolName : String
    lateinit var mCity : String
    lateinit var mState : String
    lateinit var mClass : String
    lateinit var mSchoolID : String
    lateinit var dialog : Dialog
    lateinit var mSchoolDetailsTV : TextView
    lateinit var mSchoolErrorTV : TextView
    lateinit var mChooseSchoolDialog: BottomSheetDialog
    lateinit var mDialogView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)
        mChooseSchoolDialog= BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        mDialogView= layoutInflater.inflate(R.layout.bottom_sheet_signup,null)
        mChooseSchoolDialog.setContentView(mDialogView)

        mSchoolDetailsTV = findViewById(R.id.school_details)
        mSchoolErrorTV = mDialogView.findViewById(R.id.school_error)

        if (applicationContext.packageName == "com.home.coexcleducation") {
            header_background.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.coexcl_header_one))
            header_background.alpha = 0.5f
        } else {
            welcome.visibility = View.GONE
            header_background.alpha = 0.5f
            header_background.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cofi_header_back))
        }



        mobile.setText(intent.getStringExtra("mobile"))

        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))

        create_account.setOnClickListener{
            mName = name.text.toString()
            mMobile = mobile.text.toString()
            mPassword = password.text.toString()
            mConfirmPassword = confirm_password.text.toString()
            mSchoolID = mDialogView.school_id.text.toString()

            if(!mName.isNullOrEmpty() && !mMobile.isNullOrEmpty() && !mPassword.isNullOrEmpty() && !mConfirmPassword.isNullOrEmpty()) {

                if(mPassword.length >= 6) {
                    if (mConfirmPassword == mPassword) {
                        password.error = null
                        confirm_password.error = null
                        Signup().execute()
                    } else {
                        confirm_password.error = "Password mismatch"
                    }
                } else {
                    password.error = "Password must be min 6 digits"
                }
            } else {
                ViewUtils().displayToast("Please provide all the details", "failure", this, "")
            }
        }

        choose_your_school.setOnClickListener{
            mChooseSchoolDialog.show()
        }

        mDialogView.find_School.setOnClickListener{
            mSchoolID = mDialogView.school_id.text.toString()
            if(!mSchoolID.isNullOrEmpty()) {
                if(mSchoolID.length >= 4) {
                    FindSchoolCode().execute()
                } else {
                    ViewUtils().displayToast("Please provide valid School code", "failure", this, "")
                }
            } else {
                ViewUtils().displayToast("Please provide your School code", "failure", this, "")
            }
        }


        val items = listOf("Class 6", "Class 7", "Class 8", "Class 9", "Class 10", "Class 10+1", "Class 10+2")
        val adapter = ArrayAdapter(this, R.layout.classes_item_layout, items)
        class_spinner.setAdapter(adapter)

        class_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(class_spinner.selectedItem.toString() == "Class 6")
                    mClass = "6"
                else if(class_spinner.selectedItem.toString() == "Class 7")
                    mClass = "7"
                else if(class_spinner.selectedItem.toString() == "Class 8")
                    mClass = "8"
                else if(class_spinner.selectedItem.toString() == "Class 9")
                    mClass = "9"
                else if(class_spinner.selectedItem.toString() == "Class 10")
                    mClass = "10"
                else if(class_spinner.selectedItem.toString() == "Class 10+1")
                    mClass = "11"
                else if(class_spinner.selectedItem.toString() == "Class 10+2")
                    mClass = "12"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    fun constructParamsForSignUp(): String {
        try {
            val lWriter: Writer = StringWriter()

            val mReqMap: HashMap<String, Any> = hashMapOf()
            mReqMap["name"] = mName
            mReqMap["mobile"] = mMobile
            mReqMap["password"] = mPassword
            mReqMap["class"] = mClass
            mReqMap["schoolId"] =  mSchoolID

            val mAcademicsMap: HashMap<String, Any> = hashMapOf()
            mAcademicsMap["class"] = mClass

            mReqMap["academics"] = mAcademicsMap

            val mSchoolsMap: HashMap<String, Any> = hashMapOf()
            if(!mSchoolID.isNullOrEmpty()) {
                mSchoolsMap["schoolName"] = mSchoolName
                mSchoolsMap["schoolCode"] = mSchoolID
                mSchoolsMap["city"] = mCity
                mSchoolsMap["state"] = mState
                mReqMap["subscribed"] = true
            } else {
                mSchoolsMap["schoolName"] = "Coexcl"
                mSchoolsMap["schoolCode"] = "COX2021001"
                mSchoolsMap["city"] = "Bihar"
                mSchoolsMap["state"] = "India"
                mReqMap["subscribed"] = false
            }

            mReqMap["schoolInfo"] = mSchoolsMap

            ObjectMapper().writeValue(lWriter, mReqMap)
            return lWriter.toString()
        } catch (e: java.lang.Exception) {
            CoexclLogs.printException(e)
        }
        return ""
    }

    inner class Signup : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ViewUtils().displayProgressDialog("Creating Account...", this@SignupActivity)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(this@SignupActivity, "", "Home", "Signup Started")
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = HttpHelper()
                lHttpHelper.payload = constructParamsForSignUp()
                lHttpHelper = SignUpUtils().signup(this@SignupActivity, lHttpHelper)
                CoexclLogs.errorLog(TAG, "Req from Signup - " + lHttpHelper.payload)
                CoexclLogs.errorLog(TAG, "Response from Signup - " + lHttpHelper.response)
                return lHttpHelper.response
            } catch (e : Exception) {
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
                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
                        JobIntentService.enqueueWork(this@SignupActivity, RegistrationIntentService::class.java, ApiConstant.JOB_INTENT_REGISTRATION_ID, Intent())
                        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@SignupActivity, "", "Home", "Signup")
                    } else {
                        ViewUtils().displayToast(lResponseObject.get("msg").toString(), "failure", this@SignupActivity, "")
                        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@SignupActivity, "", "Home", "Signup Invalid Mobile")
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@SignupActivity, "")
                    FirebaseAnalyticsCoexcl().logFirebaseEvent(this@SignupActivity, "", "Home", "Signup Failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    inner class FindSchoolCode : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ViewUtils().displayProgressDialog("Finding Your School...", this@SignupActivity)
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = SignUpUtils().findSchool(this@SignupActivity, mSchoolID)
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
            if(mChooseSchoolDialog.isShowing)
                mChooseSchoolDialog.dismiss()
            mSchoolDetailsTV.text = mSchoolID
            try {
                if (!result.isNullOrEmpty()) {
                    var lMapper = ObjectMapper()
                    var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                    if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                        var data = lResponseObject["data"] as HashMap<String, Any>
                        if (data.containsKey("result")) {
                            var result = data.get("result") as HashMap<String, String>
                            mSchoolName = if(result.containsKey("schoolname")) result["schoolname"] as String else ""
                            mCity = if(result.containsKey("city")) result["city"] as String else ""
                            mState = if(result.containsKey("state")) result["state"] as String else ""
                            mSchoolDetailsTV.text = mSchoolName+", "+mCity
                            mSchoolErrorTV.visibility = View.GONE
                            FirebaseAnalyticsCoexcl().logFirebaseEvent(this@SignupActivity, "", "Home", "School_fetch_by_code success")
                            if(mChooseSchoolDialog.isShowing)
                                mChooseSchoolDialog.dismiss()
                        } else {
                            mSchoolErrorTV.visibility = View.VISIBLE
                            mSchoolErrorTV.text = "No school found"
                            FirebaseAnalyticsCoexcl().logFirebaseEvent(this@SignupActivity, "", "Home", "School_fetch_by_code not_found")
                        }
                    } else {
                        ViewUtils().displayToast("Something went wrong", "failure", this@SignupActivity, "")
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@SignupActivity, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}