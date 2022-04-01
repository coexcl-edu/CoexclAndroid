package com.home.coexcleducation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.comming_soon_layout.*

class ComingSoonActivty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comming_soon_layout)

        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }
}