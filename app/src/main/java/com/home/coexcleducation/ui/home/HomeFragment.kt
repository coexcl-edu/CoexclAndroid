package com.home.coexcleducation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.freshchat.consumer.sdk.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.quiz.QuizHomeActivity
import com.home.coexcleducation.ui.fanfacts.FunFactsActivity
import com.home.coexcleducation.ui.liveclass.LiveClassListActivity
import com.home.coexcleducation.ui.notes.NoteListingActivity
import com.home.coexcleducation.ui.school.SchoolActivity
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import im.crisp.client.ChatActivity
import kotlinx.android.synthetic.main.dashboard_screen.view.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.dashboard_screen, container, false)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)

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


//        Glide.with(requireActivity())
//            .load(UserDetails.getInstance().profileImage)
//            .centerCrop()
//            .circleCrop()
//            .placeholder(R.drawable.kid_place_holder)
//            .into(root.profile_image);


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


        root.ask_doubt.setOnClickListener{
            val tags: MutableList<String> = ArrayList()
            tags.add("order_queries")
            val options = ConversationOptions().filterByTags(tags, "Order Queries")
            Freshchat.showConversations(requireContext(), options);
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireActivity(), "", "Home", "AskDoubts")

//            val crispIntent = Intent(requireActivity(), ChatActivity::class.java)
//            startActivity(crispIntent)
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



        return root
    }
}


