package com.home.coexcleducation.ui.videos

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.home.coexcleducation.R
import com.home.coexcleducation.ui.VideoPlayerView
import com.home.coexcleducation.ui.adaptar.ListAdapter
import com.home.coexcleducation.ui.lession.SubjectDetailActivtiy
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl
import com.home.coexcleducation.utils.Subject
import kotlinx.android.synthetic.main.fragment_videos.view.*


class LearnFragment : Fragment() {

    private lateinit var dashboardViewModel: VideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(VideoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_videos, container, false)

        val window: Window = requireActivity().window
        val background = resources.getDrawable(R.drawable.app_background_gradient)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(resources.getColor(android.R.color.transparent))
        window.setBackgroundDrawable(background)

        root.maths.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", Subject.MATHS)
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Subject Maths")
        }

        root.physics.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", Subject.PHYSICS)
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Subject Physics")
        }

        root.chemistry.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", Subject.CHEMISTRY)
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Subject Chemistry")
        }

        root.biology.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", Subject.BIOLOGY)
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Subject Bilogy")
        }

        root.stem.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", Subject.STEM)
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Subject stem")
        }

        root.currentAffair.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", Subject.CURRENT_AFFAIRS)
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            FirebaseAnalyticsCoexcl().logFirebaseEvent(requireContext(), "", "Home", "Subject Current Affairs")
        }


        return root
    }
}