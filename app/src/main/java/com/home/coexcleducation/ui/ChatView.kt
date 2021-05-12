package com.home.coexcleducation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import kotlinx.android.synthetic.main.*
import kotlinx.android.synthetic.main.chat_support.*

class ChatView : AppCompatActivity() {
    companion object {
        const val PAGE_URL = "https://tawk.to/chat/6058b7daf7ce18270932b0e9/1f1d8sdgl"
        const val MAX_PROGRESS = 100
    }
    private lateinit var pageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_support)
        // get pageUrl from String
        pageUrl = "https://tawk.to/chat/6058b7daf7ce18270932b0e9/1f1d8sdgl"
        initWebView()
        setWebClient()
        handlePullToRefresh()
        loadUrl(pageUrl)
    }

    private fun handlePullToRefresh() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        webView.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }
    private fun setWebClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                    view: WebView,
                    newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                progressbar.progress = newProgress
                if (newProgress < MAX_PROGRESS && progressbar.visibility == ProgressBar.GONE) {
                    progressbar.visibility = ProgressBar.VISIBLE
                }
                if (newProgress == MAX_PROGRESS) {
                    progressbar.visibility = ProgressBar.GONE
                }
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, exit the activity)
        return super.onKeyDown(keyCode, event)
    }
    private fun loadUrl(pageUrl: String) {
        webView.loadUrl(pageUrl)
    }
}