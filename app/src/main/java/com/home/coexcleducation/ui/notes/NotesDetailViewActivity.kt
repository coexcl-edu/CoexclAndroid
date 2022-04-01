package com.home.coexcleducation.ui.notes

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.note_for_customer_layout.*
import kotlinx.android.synthetic.main.notes_detail_view.*
import org.apache.commons.text.StringEscapeUtils

class NotesDetailViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes_detail_view)

        back.setOnClickListener{
            ViewUtils().exitActivityToRight(this)
        }

        edit.setOnClickListener{
            val lIntent = Intent(this@NotesDetailViewActivity, EditNotesActivity::class.java)
            lIntent.putExtra("title", intent.getStringExtra("title"))
            lIntent.putExtra("desc", intent.getStringExtra("desc"))
            lIntent.putExtra("id", intent.getStringExtra("id"))
            lIntent.putExtra("newNote", false)
            startActivity(lIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            finish()
        }

        renderData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewUtils().exitActivityToRight(this)
    }

    private fun renderData() {
        content.setText(StringEscapeUtils.unescapeHtml4(intent.getStringExtra("desc").toString()))
        header.setText(StringEscapeUtils.unescapeHtml4(intent.getStringExtra("title").toString()))

        content.movementMethod = ScrollingMovementMethod()
    }

}