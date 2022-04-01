package com.home.coexcleducation.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.home.coexcleducation.MainApplication
import com.home.coexcleducation.R
import com.home.coexcleducation.database.NotificationTable
import com.home.coexcleducation.jdo.NotificationJDO
import com.home.coexcleducation.ui.adaptar.NotificationAdaptar
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.PreferenceHelper
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        ViewUtils().setWindowBackground(requireActivity())

        try {

            var lListOfnotification = NotificationTable(requireContext()).all as List<NotificationJDO>
            CoexclLogs.errorLog("Notification Fragment", "lListOfnotification - "+lListOfnotification)
            if (lListOfnotification.isNullOrEmpty()) {
                root.notification_error.visibility = View.VISIBLE
            } else {
                var adaptar = NotificationAdaptar(requireContext(), lListOfnotification.reversed())
                root.notification_list.adapter = adaptar
                root.notification_error.visibility = View.GONE
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Notification")
        return root
    }

}