package com.home.coexcleducation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.home.coexcleducation.jdo.UserDetails

class FirebaseAnalyticsCoexcl {

    fun logFirebaseEvent(pContext: Context?, pId: String?, pName: String?, pEvent: String?) {
        try {
                val mFirebaseAnalytics = FirebaseAnalytics.getInstance(pContext!!)
                val lBundle = Bundle()
                lBundle.putString(FirebaseAnalytics.Param.ITEM_ID, pId)
                lBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, pName)
                lBundle.putString(FirebaseAnalytics.Param.CONTENT, UserDetails.getInstance().id)
                mFirebaseAnalytics.logEvent(pEvent!!, lBundle)

        } catch (e: Exception) {
            CoexclLogs.printException(e)
        }
    }
}