package com.home.coexcleducation.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.home.coexcleducation.R
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    private lateinit var profileViewModel: SettingsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        requireActivity().window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))


        root.about_layout.setOnClickListener{
            startActivity(Intent(requireActivity(), AboutLayout::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        root.progress_report.setOnClickListener{
            startActivity(Intent(requireActivity(), ProgressLayout::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
        return root
    }
}