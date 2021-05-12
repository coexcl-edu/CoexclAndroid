package com.home.coexcleducation.ui

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.home.coexcleducation.R
import com.home.coexcleducation.ui.adaptar.ListAdapter
import kotlinx.android.synthetic.main.video_view_layout.*

class VideoPlayerView : AppCompatActivity() {

    lateinit var context : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_view_layout)
        context = this;

        val list = arrayListOf<String>()

        list.addAll(
            listOf(
                "Force of Friction",
                "Factors Affecting Friction",
                "Friction: A Necessary Evil",
                "Wheels Reduce Friction",
                "Fluid Friction",
                "Lightning",
                "Charging by Rubbing",
                "Transfer of Charge",
                "Lightning Safety",
                "Earthquakes"
            )
        )
        var adapter = ListAdapter(context, list)
        video_listview.adapter = adapter
    }
}