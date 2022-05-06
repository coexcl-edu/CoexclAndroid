package com.home.coexcleducation.premium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.coexcleducation.R

class PremiumDisplayFragment(intValue : Int) : Fragment() {

    lateinit var lSettingView : View;
    var mTypeOfShowCase = intValue

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(mTypeOfShowCase == 0) {
            lSettingView = inflater.inflate(R.layout.premium_fearure_showcase, container, false);
        } else if(mTypeOfShowCase == 1) {
            lSettingView = inflater.inflate(R.layout.premium_fearure_showcase, container, false);
        } else if(mTypeOfShowCase == 2) {
            lSettingView = inflater.inflate(R.layout.premium_fearure_showcase, container, false);
        } else if(mTypeOfShowCase == 3) {
            lSettingView = inflater.inflate(R.layout.premium_fearure_showcase, container, false);
        } else if(mTypeOfShowCase == 4) {
            lSettingView = inflater.inflate(R.layout.premium_fearure_showcase, container, false);
        } else if(mTypeOfShowCase == 5) {
            lSettingView = inflater.inflate(R.layout.premium_fearure_showcase, container, false);
        } else if(mTypeOfShowCase == 6) {
            lSettingView = inflater.inflate(R.layout.premium_fearure_showcase, container, false);
        }

        return lSettingView
    }
}

