package com.home.coexcleducation.ui.bookliveclass

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.home.coexcleducation.R
import com.home.coexcleducation.utils.ApiConstant
import com.home.coexcleducation.utils.ViewUtils
import kotlinx.android.synthetic.main.choose_liveclass.*

class ListOfLiveClassForBooking : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_liveclass)

        var subject = intent.getStringExtra("subject")
        var meeting60 = ApiConstant.MATHS_60mins
        var meeting45 = ApiConstant.MATHS_45mins
        var meeting30 = ApiConstant.MATHS_30mins
        var meeting15 = ApiConstant.MATHS_15mins

        name.text = "Choose Session Duration"

        if(subject.equals("maths")) {
            meeting60 = ApiConstant.MATHS_60mins
            meeting45 = ApiConstant.MATHS_45mins
            meeting30 = ApiConstant.MATHS_30mins
            meeting15 = ApiConstant.MATHS_15mins
//            name.text = "Choose Class Duration"
            liveclas60.text = "Maths Session for 60min"
            liveclas45.text = "Maths Session for 45min"
            liveclas30.text = "Maths Session for 30min"
            liveclas15.text = "Maths Session for 15min"
            class_60_image.setImageResource(R.drawable.ic_subject_maths)
            class_45_image.setImageResource(R.drawable.ic_subject_maths)
            class_30_image.setImageResource(R.drawable.ic_subject_maths)
            class_15_image.setImageResource(R.drawable.ic_subject_maths)
        } else if(subject.equals("physics")) {
//            name.text = "Physics Live Class"
            meeting30 = ApiConstant.PHYSICS_30mins
            meeting60 = ApiConstant.PHYSICS_60mins
            meeting45 = ApiConstant.PHYSICS_45mins
            meeting15 = ApiConstant.PHYSICS_15mins
            liveclas60.text = "Physics Session for 60min"
            liveclas45.text = "Physics Session for 45min"
            liveclas30.text = "Physics Session for 30min"
            liveclas15.text = "Physics Session for 15min"
            class_60_image.setImageResource(R.drawable.ic_subject_physics)
            class_45_image.setImageResource(R.drawable.ic_subject_physics)
            class_30_image.setImageResource(R.drawable.ic_subject_physics)
            class_15_image.setImageResource(R.drawable.ic_subject_physics)
        } else if(subject.equals("chemistry")) {
//            name.text = "Chemistry Live Class"
            meeting60 = ApiConstant.CHEMISTRY_60mins
            meeting45 = ApiConstant.CHEMISTRY_45mins
            meeting30 = ApiConstant.CHEMISTRY_30mins
            meeting15 = ApiConstant.CHEMISTRY_15mins
            liveclas60.text = "Chemistry Session for 60min"
            liveclas45.text = "Chemistry Session for 45min"
            liveclas30.text = "Chemistry Session for 30min"
            liveclas15.text = "Chemistry Session for 15min"
            class_60_image.setImageResource(R.drawable.ic_subject_chemistry)
            class_45_image.setImageResource(R.drawable.ic_subject_chemistry)
            class_30_image.setImageResource(R.drawable.ic_subject_chemistry)
            class_15_image.setImageResource(R.drawable.ic_subject_chemistry)
        } else if(subject.equals("biology")) {
//            name.text = "Biology Live Class"
            meeting30 = ApiConstant.BIOLOGY_30mins
            meeting45 = ApiConstant.BIOLOGY_45mins
            meeting60 = ApiConstant.BIOLOGY_60mins
            meeting15 = ApiConstant.BIOLOGY_15mins
            liveclas60.text = "Biology Session for 60min"
            liveclas45.text = "Biology Session for 45min"
            liveclas30.text = "Biology Session for 30min"
            liveclas15.text = "Biology Session for 15min"
            class_60_image.setImageResource(R.drawable.ic_subject_biology)
            class_45_image.setImageResource(R.drawable.ic_subject_biology)
            class_30_image.setImageResource(R.drawable.ic_subject_biology)
            class_15_image.setImageResource(R.drawable.ic_subject_biology)
        }

        back.setOnClickListener{
            exit()
        }

        class_60.setOnClickListener{
            val customIntent = CustomTabsIntent.Builder()
            customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            ViewUtils().openCustomTab(
                this, customIntent.build(),
                Uri.parse(meeting60)
            )
        }

        class_45.setOnClickListener{
            val customIntent = CustomTabsIntent.Builder()
            customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            ViewUtils().openCustomTab(
                this, customIntent.build(),
                Uri.parse(meeting45)
            )
        }

        class_30.setOnClickListener{
            val customIntent = CustomTabsIntent.Builder()
            customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            ViewUtils().openCustomTab(
                this, customIntent.build(),
                Uri.parse(meeting30)
            )
        }

        class_15.setOnClickListener{
            val customIntent = CustomTabsIntent.Builder()
            customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            ViewUtils().openCustomTab(
                this, customIntent.build(),
                Uri.parse(meeting15)
            )
        }

    }

    fun exit() {
        finish()
        ViewUtils().exitActivityToRight(this)
    }

    override fun onBackPressed() {
        exit()
    }
}