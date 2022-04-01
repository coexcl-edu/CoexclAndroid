package com.home.coexcleducation.ui.registration

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.home.coexcleducation.utils.CoexclLogs


class SMSBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        CoexclLogs.errorLog("SMSBroadCast", "SMS message")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent!!.action) {
            val extras = intent!!.extras
            var message = ""

            val status: Status? = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status!!.getStatusCode()) {
                CommonStatusCodes.SUCCESS ->           // Get SMS message contents
                {
                    message = extras!![SmsRetriever.EXTRA_SMS_MESSAGE] as String
                    CoexclLogs.errorLog("SMSBroadCast", "SMS message $message")
                }
                CommonStatusCodes.TIMEOUT -> {
                }
            }
        }
    }
}