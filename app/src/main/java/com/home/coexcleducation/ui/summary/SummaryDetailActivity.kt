package com.home.coexcleducation.ui.summary

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.summary_details_layout.*


class SummaryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.summary_details_layout)

//        var dialog = ViewUtils().displayProgressDialog("Loading your content..", this )
//        webview.settings.javaScriptEnabled = true
//        val filename = "http://www.phys.ens.fr/~ebrunet/Thermo-en.pdf"
//        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=$filename")
//
//        webview.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                dialog.dismiss()
//            }
//        }

        FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Summary Details")
    }
}