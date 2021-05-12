package com.home.coexcleducation.ui.home

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.home.coexcleducation.R

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
        requireActivity().window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
//        val window: Window = requireActivity().window
//        val background = resources.getDrawable(R.drawable.video_background_gradient)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.setStatusBarColor(resources.getColor(android.R.color.transparent))
//        window.setBackgroundDrawable(background)



        return root
    }
}


