package com.home.coexcleducation.ui.notification

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.home.coexcleducation.MainApplication
import com.home.coexcleducation.database.NotificationTable
import com.home.coexcleducation.jdo.NotificationJDO
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.PreferenceHelper
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import java.util.*

class NotificationReciever : OneSignal.OSRemoteNotificationReceivedHandler {
    override fun remoteNotificationReceived(context: Context, notificationReceivedEvent: OSNotificationReceivedEvent?) {
        CoexclLogs.errorLog("Notification", "######## FCM RECIEVE " + notificationReceivedEvent.toString())
        try {
            var lJDo = NotificationJDO()
            lJDo.id = notificationReceivedEvent!!.notification.notificationId
            lJDo.title = notificationReceivedEvent!!.notification.title
            lJDo.message = notificationReceivedEvent!!.notification.body
            lJDo.image = notificationReceivedEvent!!.notification.bigPicture
            lJDo.timeStamp = Date().time
            NotificationTable(context).insert(lJDo);

            val count = PreferenceHelper.getSharedPreference(context).getInt(PreferenceHelper.NOTIFICATION_COUNT, 0)
            PreferenceHelper.getSharedPreference(context).edit().putInt(PreferenceHelper.NOTIFICATION_COUNT, count + 1).commit()

            val lUpdateBroadCast = Intent()
            lUpdateBroadCast.action = "com.coexcl.notification"
            LocalBroadcastManager.getInstance(context).sendBroadcast(lUpdateBroadCast)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}