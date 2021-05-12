package com.home.coexcleducation.ui.registration

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.home.coexcleducation.MainActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.httphelper.HttpClient
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.signup_layout.*
import java.io.StringWriter
import java.io.Writer
import java.util.HashMap

class SignupActivity : AppCompatActivity() {

    var TAG = "SignupActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent))

        create_account.setOnClickListener{
            Signup().execute()
        }


        class_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    fun constructParamsForSignUp(): String {
        try {
            val lWriter: Writer = StringWriter()

//
//            val mReqMap: HashMap<String, Any> = hashMapOf()
//            mReqMap["name"] = appointmentJDO.id
//            mReqMap["mobile"] = mTotalCost
//            mReqMap["password"] = mDiscountAmount.toString()
//            mReqMap["currency"] = ""
//            mReqMap["customerName"] =  cusotmerJDO.firstName + " " + cusotmerJDO.lastName
//            mReqMap["customerKey"] = cusotmerJDO.key
//            mReqMap["paymentGateway"] = "cash"
//            mReqMap["lineItemsInfo"] = mLineItemsList
//            ObjectMapper().writeValue(lWriter, mReqMap)
            return lWriter.toString()
        } catch (e: java.lang.Exception) {
            CoexclLogs.printException(e)
        }
        return ""
    }

    inner class Signup : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            ViewUtils().displayProgressDialog("Creating Account...", this@SignupActivity)
        }

        override fun doInBackground(vararg params: String?): String {
            var lHttpHelper = HttpHelper()
            lHttpHelper.payload = constructParamsForSignUp()
            lHttpHelper = SignUpUtils().signup(this@SignupActivity, lHttpHelper)
            CoexclLogs.errorLog(TAG, "Response from Signup - "+lHttpHelper.response)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
        }

    }

}