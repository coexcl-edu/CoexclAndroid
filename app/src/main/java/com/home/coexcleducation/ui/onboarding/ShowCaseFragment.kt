package com.home.coexcleducation.ui.onboarding

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.home.coexcleducation.R


class ShowCaseFragment(intValue: Int) : Fragment() {

    lateinit var lFeatureView : View;
    var mTypeOfShowCase = intValue

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lFeatureView = inflater.inflate(R.layout.feature_showcase, container, false);
        var lFeatureDesc = lFeatureView.findViewById<TextView>(R.id.feature_desc)
        var lFeatureLogo = lFeatureView.findViewById<ImageView>(R.id.feature_logo)


        val textShader: Shader = LinearGradient(0f, 0f, 50f, lFeatureDesc.textSize, intArrayOf(
                Color.parseColor("#2C4A93"),
                Color.parseColor("#D32A6B"),
        ), null, Shader.TileMode.MIRROR)
//        lFeatureDesc.getPaint().setShader(textShader)

        if(mTypeOfShowCase == 0) {
            lFeatureDesc.text = resources.getString(R.string.feature1)
            lFeatureLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_1))
        } else if(mTypeOfShowCase == 1) {
            lFeatureDesc.text = resources.getString(R.string.feature2)
            lFeatureLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_2))
        } else if(mTypeOfShowCase == 2) {
            lFeatureDesc.text = resources.getString(R.string.feature3)
            lFeatureLogo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.onborading_3))
        }

        return lFeatureView
    }
}

