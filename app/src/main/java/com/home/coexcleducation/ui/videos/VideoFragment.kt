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
import com.home.coexcleducation.ui.registration.SignupActivity
import kotlinx.android.synthetic.main.fragment_videos.view.*


class VideoFragment : Fragment() {

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

        val list = arrayListOf<String>()

        list.addAll(
            listOf(
                "Force of Friction",
                "Factors Affecting Friction",
                "Friction: A Necessary Evil",
                "Wheels Reduce Friction",
                "Fluid Friction",
                "Lightning",
                "Charging by Rubbing",
                "Transfer of Charge",
                "Lightning Safety",
                "Earthquakes"
            )
        )
//        var adapter = ListAdapter(requireContext(), list)
//        root.findViewById<ListView>(R.id.video_listview).adapter = adapter
//
//        root.video_listview.onItemClickListener = OnItemClickListener {
//                parent, view, position, id ->
//            var intent = Intent(requireActivity(), VideoPlayerView::class.java)
//            startActivity(intent)
//        }

        root.maths.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", "Mathematics")
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.physics.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", "Physics")
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.chemistry.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", "Chemistry")
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.biology.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", "Biology")
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.stem.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", "S.T.E.M")
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.currentAffair.setOnClickListener{
            var lIntent = Intent(requireActivity(), SubjectDetailActivtiy::class.java)
            lIntent.putExtra("subject", "Current Affairs")
            startActivity(lIntent)
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }



        return root
    }
}