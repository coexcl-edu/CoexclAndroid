package com.home.coexcleducation.ui.settings

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.freshchat.consumer.sdk.Freshchat
import com.home.coexcleducation.R
import com.home.coexcleducation.database.NotificationTable
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.premium.PremiumDetailView
import com.home.coexcleducation.premium.UpgradePlanActivity
import com.home.coexcleducation.ui.ComingSoonActivty
import com.home.coexcleducation.ui.registration.LoginActivity
import com.home.coexcleducation.utils.CoexclLogs
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.PreferenceHelper
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    private lateinit var profileViewModel: SettingsViewModel
    var termsLink = "https://info.coexcl.com/privacypolicy/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        requireActivity().window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

        if(UserDetails.getInstance().schoolCode == "COX2100001") {
            root.upgrade_premum_layout.visibility = View.VISIBLE
            root.premium_divider.visibility = View.VISIBLE
            if (UserDetails.getInstance().subscribed) {
                root.upgrade_premum.text = "Premium Member"
                root.premium_badge.visibility = View.VISIBLE
            } else {
                root.upgrade_premum.text = "Upgrade Now"
                root.premium_badge.visibility = View.INVISIBLE
            }
        } else {
            root.upgrade_premum_layout.visibility = View.GONE
            root.premium_divider.visibility = View.GONE
        }

        root.upgrade_premum_layout.setOnClickListener {
//            if (UserDetails.getInstance().subscribed) {
//                startActivity(Intent(requireActivity(), PremiumDetailView::class.java))
//            } else {
                startActivity(Intent(requireActivity(), UpgradePlanActivity::class.java))
//            }
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.settings_layout.setOnClickListener{
            startActivity(Intent(requireActivity(), ComingSoonActivty::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.terms_layout.setOnClickListener {
            val customIntent = CustomTabsIntent.Builder()
            customIntent.setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary))
            ViewUtils().openCustomTab(requireActivity(), customIntent.build(),
                    Uri.parse(termsLink))
        }

        root.contact_us.setOnClickListener{
            startActivity(Intent(requireActivity(), ContactUs::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.share_app_layout.setOnClickListener{
            val lShareIntent = Intent(Intent.ACTION_SEND)
            lShareIntent.type = "text/plain"
            lShareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, i am using Coexcl Education App. " +
                    "Download app link: https://play.google.com/store/apps/details?id=com.home.coexcleducation")
            startActivity(Intent.createChooser(lShareIntent, "Share using"))
        }

        root.rate_us_layout.setOnClickListener{
             var appPackageName = requireActivity().getPackageName();
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }

        root.faq.setOnClickListener{
            startActivity(Intent(requireActivity(), FAQaActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.about_layout.setOnClickListener{
            startActivity(Intent(requireActivity(), AboutLayout::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.progress_report.setOnClickListener{
            startActivity(Intent(requireActivity(), ProgressLayout::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.logout.setOnClickListener{
            displayAlertDialogOnSignOut()
        }

        FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Settings")
        return root
    }


    fun displayAlertDialogOnSignOut() {
            try {
                val alertConfirm = AlertDialog.Builder(requireContext())
                alertConfirm.setTitle("Log Out")
                alertConfirm.setMessage("Are your sure you want to log out?")
                alertConfirm.setCancelable(true)
                alertConfirm.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                alertConfirm.setPositiveButton("Logout") { dialog, which ->
                    FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Signout")
                    dialog.dismiss()
                    UserDetails.getInstance().isLoggedIn = false
                    Freshchat.resetUser(context);
                    PreferenceHelper(requireContext()).clearAllPreferenceData()
                    NotificationTable(requireContext()).deleteAllRecord()
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
                    requireActivity().finishAffinity()
                }
                val dialog = alertConfirm.create()
                dialog.show()
            } catch (e : java.lang.Exception) {
                e.printStackTrace()
            }

//        try {
//            val dialog = Dialog(requireActivity(), R.style.DialogCustomTheme)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(R.layout.alert_popup)
//            dialog.setCanceledOnTouchOutside(false)
//            dialog.setCancelable(false)
//            val window = dialog.window
//            window?.setGravity(Gravity.CENTER)
//            window?.setLayout((requireActivity().getWindow().peekDecorView().width * 0.85).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
//            val lAlterMessage = dialog.findViewById<TextView>(R.id.Alert_Message)
//            val leftBtnText = dialog.findViewById<TextView>(R.id.Dialog_LeftBtn)
//            val rightBtnText = dialog.findViewById<TextView>(R.id.Dialog_RightBtn)
//            val msg = dialog.findViewById<TextView>(R.id.Alert_Title)
//            val lYesButton = dialog.findViewById<LinearLayout>(R.id.ConfirmLayout)
//            val lNoButton = dialog.findViewById<LinearLayout>(R.id.CancelLayout)
//            lAlterMessage.text = "Are your sure you want to log out? "
//            leftBtnText.text = "No"
//            rightBtnText.text = "Log Out"
//            msg.text = "Log Out"
//            lNoButton.setOnClickListener {
//                dialog.dismiss()
//            }
//            lYesButton.setOnClickListener {
//                FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Signout")
//                dialog.dismiss()
//                UserDetails.getInstance().isLoggedIn = false
//                Freshchat.resetUser(context);
//                PreferenceHelper(requireContext()).clearAllPreferenceData()
//                NotificationTable(requireContext()).deleteAllRecord()
//                startActivity(Intent(requireActivity(), LoginActivity::class.java))
//                requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
//                requireActivity().finishAffinity()
//            }
//            dialog.show()
//        } catch (e: Exception) {
//            CoexclLogs.printException(e);
//        }
    }
}