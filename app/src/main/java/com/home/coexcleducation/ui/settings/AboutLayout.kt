package com.home.coexcleducation.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.about_layout.*

class AboutLayout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_layout)

        ViewUtils().setWindowBackground(this)

        var stringbuilder =  StringBuilder()
        stringbuilder.append("Version: 2.0.2.20220508").append("\n")
        stringbuilder.append("Released Year: 2021").append("\n")
        stringbuilder.append("All Rights Reserved © COEXCL SERVICES PVT LTD.").append("\n").append("\n")
        stringbuilder.append("In Association with").append("")

        if (applicationContext.packageName === "com.home.coexcleducation") {
            association_brand.visibility = View.GONE
        } else {
            association_brand.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cofi_association))
        }


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