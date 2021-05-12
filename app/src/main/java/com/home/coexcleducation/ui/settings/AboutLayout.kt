package com.home.coexcleducation.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.about_layout.*
import java.lang.StringBuilder

class AboutLayout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_layout)

        var stringbuilder =  StringBuilder()
        stringbuilder.append("Version: 2.0.1").append("\n")
        stringbuilder.append("Released Year: 2021").append("\n")
        stringbuilder.append("All Rights Reserved © COEXCL SERVICES PVT LTD.").append("")

        sub_header.text = stringbuilder.toString()

        back.setOnClickListener{
            exit()
        }

    }

    fun exit() {
        finish()
        ViewUtils().exitActivityToRight(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exit()
    }
}