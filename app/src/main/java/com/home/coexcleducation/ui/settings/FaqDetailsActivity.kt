package com.home.coexcleducation.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.faq_detail_layout.*

class FaqDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.faq_detail_layout)

        ViewUtils().setWindowBackground(this)

        question.setText(intent.getStringExtra("question"))
        answer.setText(intent.getStringExtra("answer"))

        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }
}