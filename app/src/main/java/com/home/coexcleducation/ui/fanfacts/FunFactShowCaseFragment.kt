package com.home.coexcleducation.ui.fanfacts

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.Constants
import java.util.*


class FunFactShowCaseFragment(map: HashMap<String, String>) : Fragment() {

    lateinit var lFeatureView : View;
    var mMap = map

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lFeatureView = inflater.inflate(R.layout.funfactshowcase, container, false);
        var lFeatureDesc = lFeatureView.findViewById<TextView>(R.id.feature_desc)
        var lFeatureLogo = lFeatureView.findViewById<ImageView>(R.id.feature_logo)
        var lBack = lFeatureView.findViewById<LinearLayout>(R.id.background)

        lFeatureDesc.text = mMap.get("description")
        lFeatureDesc.movementMethod = ScrollingMovementMethod()

        var lUrl = mMap.get("fileUrl")
        if (lUrl != null && !lUrl.isEmpty()) {
            Glide.with(requireActivity())
                    .load(lUrl)
                    .placeholder(R.drawable.loader_image)
                    .into(lFeatureLogo)
        }

        lBack.backgroundTintList  = ContextCompat.getColorStateList(requireContext(), Constants().randomColour)

        return lFeatureView
    }
}
