package com.home.coexcleducation.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.home.coexcleducation.BuildConfig
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.contact_us.*
import java.text.SimpleDateFormat
import java.util.*

class ContactUs : AppCompatActivity() {

    var contactUsLink = "https://coexcl.com/contact"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_us)
        ViewUtils().setWindowBackground(this)

        contact_detail.setOnClickListener{
            val customIntent = CustomTabsIntent.Builder()
            customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            ViewUtils().openCustomTab(
                this, customIntent.build(),
                Uri.parse(contactUsLink)
            )
        }

        feedback.setOnClickListener{
            sendFeedback()
        }

        back.setOnClickListener{
            exit()
        }
    }

    fun exit() {
        finish()
        ViewUtils().exitActivityToRight(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exit()
    }

    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SEND)
        //                intent.setData(Uri.parse("mailto:"));
        intent.type = "text/html"
        intent.setPackage("com.google.android.gm")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@coexcl.in"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android App Feedback")
        val lFeedbackSB = StringBuilder()
        lFeedbackSB.append("Feedback : Coexcl Android\n")
        lFeedbackSB.append("----------------------------------------------\n\n\n\n\n\n\n")
        lFeedbackSB.append(UserDetails.getInstance().name + "\n")
        lFeedbackSB.append(UserDetails.getInstance().mobile + "\n")
        lFeedbackSB.append(BuildConfig.VERSION_NAME.toString() + "(" + BuildConfig.VERSION_CODE + ")\n")
        lFeedbackSB.append(""" ${Build.MODEL}""".trimIndent()+ "\n")
        lFeedbackSB.append("""Android ${Build.VERSION.SDK_INT} """.trimIndent()+ "\n")
        lFeedbackSB.append("""${TimeZone.getDefault().displayName}${SimpleDateFormat(", zzz", Locale.ENGLISH).format(Date())} """.trimIndent()+ "\n")
        intent.putExtra(Intent.EXTRA_TEXT, lFeedbackSB.toString())
        //                intent.setType("message/rfc822");
        val chooserIntent = Intent.createChooser(intent, "Send Feedback")
        try {
            startActivity(chooserIntent)
        } catch (e: ActivityNotFoundException) {
            intent.setPackage(null)
            val chooserInt = Intent.createChooser(intent, "Send Feedback")
            startActivity(chooserInt)
        }
        FirebaseAnalyticsCoexcl().logFirebaseEvent(
            this,
            "",
            "Feedback",
            "Feedback"
        )
    }
}