package com.home.coexcleducation.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.ObjectMapper
import com.freshchat.consumer.sdk.Freshchat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.home.coexcleducation.R
import com.home.coexcleducation.database.NotificationTable
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.intercom.IntercomHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.premium.UpgradePlanActivity
import com.home.coexcleducation.quiz.QuizHomeActivity
import com.home.coexcleducation.ui.fanfacts.FunFactsActivity
import com.home.coexcleducation.ui.liveclass.LiveClassListActivity
import com.home.coexcleducation.ui.notes.NoteListingActivity
import com.home.coexcleducation.ui.registration.LoginActivity
import com.home.coexcleducation.ui.registration.SignUpUtils
import com.home.coexcleducation.ui.school.SchoolActivity
import com.home.coexcleducation.utils.*
import kotlinx.android.synthetic.main.dashboard_screen.view.*
import java.io.StringWriter
import java.io.Writer


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sessionText : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.dashboard_screen, container, false)

        sessionText = root.session_count

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.status_bar)

        FirebaseCrashlytics.getInstance().setUserId(UserDetails.getInstance().id)

        root.name.text = UserDetails.getInstance().name
        root.school.text = UserDetails.getInstance().schoolName
        Glide.with(requireActivity())
            .load(UserDetails.getInstance().profileAvatar)
            .centerCrop()
            .circleCrop()
            .placeholder(R.drawable.avatar1)
            .into(root.profile_image);

        Glide.with(requireContext()).asGif()
            .load(R.raw.chat)
            .into(root.chat_image)
        Glide.with(requireContext()).asGif()
            .load(R.raw.rocket)
            .into(root.fun_fact_image)
        Glide.with(requireContext()).asGif()
            .load(R.raw.notes)
            .into(root.make_notes)
//        Glide.with(requireContext()).asGif()
//            .load(R.raw.quiz_icon)
//            .into(root.quiz_logo)


//        Glide.with(requireActivity())
//            .load(UserDetails.getInstance().profileImage)
//            .centerCrop()
//            .circleCrop()
//            .placeholder(R.drawable.kid_place_holder)
//            .into(root.profile_image);

        root.book_live_class.setOnClickListener{

            val customIntent = CustomTabsIntent.Builder()
            customIntent.setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.colorAccent_light))
            ViewUtils().openCustomTab(
                requireActivity(), customIntent.build(),
                Uri.parse("https://coexclservices.setmore.com/maths-tutor")
            )
//            var lIntent = Intent(requireActivity(), ListOfSubjectForBooking::class.java)
////            var lIntent = Intent(requireActivity(), ContactUs::class.java)
//            lIntent.putExtra("subject", "Daily Quiz")
//            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "Book Live Class")
        }

        root.start_quiz.setOnClickListener{
            var lIntent = Intent(requireActivity(), QuizHomeActivity::class.java)
//            var lIntent = Intent(requireActivity(), ContactUs::class.java)
            lIntent.putExtra("subject", "Daily Quiz")
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "DailyQuiz")
        }

        root.notes.setOnClickListener{
            startActivity(Intent(requireActivity(), NoteListingActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "CreateNotes")
        }

        root.school_activity.setOnClickListener{
            startActivity(Intent(requireActivity(), SchoolActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "SchoolActivity")
        }


        root.ask_doubt.setOnClickListener {
//            val tags: MutableList<String> = ArrayList()
//            tags.add("order_queries")
//            val options = ConversationOptions().filterByTags(tags, "Order Queries")
//            Freshchat.showConversations(requireContext(), options);
//            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "AskDoubts")

            if (!UserDetails.getInstance().subscribed) {
                if (UserDetails.getInstance().sessionCount == 0) {
                    ViewUtils().displayStrongScreenForPremium(requireActivity())
                } else {
                    IntercomHelper().startIntercomChat(requireContext());
                    UpdateSession().execute()
                }
            }
        }

        root.fun_fact.setOnClickListener{
            startActivity(Intent(requireActivity(), FunFactsActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "FunFact")
        }

        root.live_class.setOnClickListener{
            startActivity(Intent(requireActivity(), LiveClassListActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "LiveClass")
        }

        showSessionCount()
        return root
    }



    private fun showSessionCount() {
        try {
            if(UserDetails.getInstance().subscribed) {
                sessionText.visibility = View.INVISIBLE
            } else {
                var session = UserDetails.getInstance().sessionCount
                if(session == 0) {
                    sessionText.text = "Monthly Free Session Over"
                } else {
                    sessionText.text = "$session/15 Free Session Left"
                }
            }
        } catch (e :Exception) {
            e.printStackTrace()
        }
    }


    inner class UpdateSession : AsyncTask<Context, String, String>() {

        override fun doInBackground(vararg params: Context): String {
            Utilty().updateSessionCount(requireContext())
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            showSessionCount()
        }
    }





}


