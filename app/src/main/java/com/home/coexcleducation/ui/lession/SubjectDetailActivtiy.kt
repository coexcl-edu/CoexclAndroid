package com.home.coexcleducation.ui.lession

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import kotlinx.android.synthetic.main.subject_details_layout.*

class SubjectDetailActivtiy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subject_details_layout)

        name.text = intent.getStringExtra("subject")

    }
}