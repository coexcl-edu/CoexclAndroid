package com.home.coexcleducation.utils

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.UserDetails
import kotlinx.android.synthetic.main.profile_update_view.*
import java.util.*

class Utilty {

    fun insertLoginData(lData: String) {
        if (!lData.isNullOrEmpty()) {
            var lMapper = ObjectMapper()
            var lResponseObject = lMapper.readValue(lData, HashMap::class.java)
            if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                var data = lResponseObject.get("data") as HashMap<String, Any>
                if (data.containsKey("userDtls")) {
                    var map: HashMap<String, Any> = data.get("userDtls") as HashMap<String, Any>
                    var lPersonalInfo: HashMap<String, String> = if(map.containsKey("personalInfo"))
                        map.get("personalInfo") as HashMap<String, String> else HashMap<String, String>()
                    var lAcademicInfo: HashMap<String, String> = if(map.containsKey("academics"))
                        map.get("academics") as HashMap<String, String> else HashMap<String, String>()
                    var lSchoolInfo: HashMap<String, String> = if(map.containsKey("schoolInfo"))
                        map.get("schoolInfo") as HashMap<String, String> else HashMap<String, String>()
                    var lQuizInfo: HashMap<String, Any> = if(map.containsKey("quizInfo"))
                        map.get("quizInfo") as HashMap<String, Any> else HashMap<String, Any>()


                    var lUserDetails = UserDetails.getInstance()
                    lUserDetails.id = if (map.containsKey("userid")) map.get("userid")
                        .toString() else ""
                    lUserDetails.mobile = if (map.containsKey("mobile")) map.get("mobile")
                        .toString() else ""

                    lUserDetails.profileImage = if (map.containsKey("profileimgurl")) map.get("profileimgurl")
                        .toString() else ""

                    lUserDetails.email = if (map.containsKey("email")) map.get("email")
                        .toString() else ""
                    lUserDetails.name =
                        if (map.containsKey("name")) map.get("name").toString() else ""
                    lUserDetails.subscribed =
                        if (map.containsKey("subscribed")) map.get("subscribed") as Boolean else false

                    lUserDetails.fatherName =
                        if (lPersonalInfo.containsKey("fatherName")) lPersonalInfo.get("fatherName")
                            .toString() else ""
                    lUserDetails.motherName =
                        if (lPersonalInfo.containsKey("motherName")) lPersonalInfo.get("motherName")
                            .toString() else ""
                    lUserDetails.parentMobile =
                            if (lPersonalInfo.containsKey("parentContact")) lPersonalInfo.get("parentContact")
                                    .toString() else ""
                    lUserDetails.bloodGroup =
                        if (lPersonalInfo.containsKey("bloodGroup")) lPersonalInfo.get("bloodGroup")
                            .toString() else ""
                    lUserDetails.hobby =
                        if (lPersonalInfo.containsKey("hobby")) lPersonalInfo.get("hobby")
                            .toString() else ""
                    lUserDetails.favSports =
                        if (lPersonalInfo.containsKey("favouriteSport")) lPersonalInfo.get("favouriteSport")
                            .toString() else ""
                    lUserDetails.gender =
                        if (lPersonalInfo.containsKey("gender")) lPersonalInfo.get("gender")
                            .toString() else ""

                    lUserDetails.className =
                        if (lAcademicInfo.containsKey("class")) lAcademicInfo.get("class")
                            .toString() else ""
                    lUserDetails.rollNo =
                        if (lAcademicInfo.containsKey("rollNo")) lAcademicInfo.get("rollNo")
                            .toString() else ""
                    lUserDetails.favSubject =
                        if (lAcademicInfo.containsKey("favouriteSubject")) lAcademicInfo.get("favouriteSubject")
                            .toString() else ""
                    lUserDetails.schoolImageUrl = if (lAcademicInfo.containsKey("logourl")) lAcademicInfo.get("logourl")
                            .toString() else ""
                    lUserDetails.schoolName =
                        if (lSchoolInfo.containsKey("schoolName")) lSchoolInfo.get("schoolName")
                            .toString() else ""
                    lUserDetails.schoolCode =
                        if (lSchoolInfo.containsKey("schoolCode")) lSchoolInfo.get("schoolCode")
                            .toString() else ""
                    lUserDetails.city = if (lSchoolInfo.containsKey("city")) lSchoolInfo.get("city")
                        .toString() else ""
                    lUserDetails.state =
                        if (lSchoolInfo.containsKey("state")) lSchoolInfo.get("state")
                            .toString() else ""
                    lUserDetails.quizLastAttempt =
                            if (lQuizInfo.containsKey("lastattempted")) lQuizInfo.get("lastattempted")
                                    .toString() else ""
                    lUserDetails.progressReport =
                            if(lQuizInfo.containsKey("percent")) lQuizInfo.get("percent").toString().toFloat();
                                else 0.0F

                    lUserDetails.isLoggedIn = true

                }
            }
        }
    }

    fun getRealPath(pContentResolver: ContentResolver, pUri: Uri): String? {
        var lPath: String? = ""
        val lCursor = pContentResolver.query(pUri, null, null, null, null)
        if (lCursor == null) lPath = pUri.path else {
            lCursor.moveToFirst()
            lPath = lCursor.getString(lCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))
            lCursor.close()
        }
        return lPath
    }

    fun getProfileAvatar(context : Context, view: ImageView) {
        try {
            val dialog = Dialog(context, R.style.DialogCustomTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.profile_avatar_layout)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
            val window = dialog.window
            window!!.setGravity(Gravity.CENTER)
            window.setLayout(
                ((context as Activity).window.peekDecorView().width * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val close = dialog.findViewById<ImageView>(R.id.close)
            val avatar1 = dialog.findViewById<ImageView>(R.id.avatar1)
            val avatar2 = dialog.findViewById<ImageView>(R.id.avatar2)
            val avatar3 = dialog.findViewById<ImageView>(R.id.avatar3)
            val avatar4 = dialog.findViewById<ImageView>(R.id.avatar4)
            val avatar5 = dialog.findViewById<ImageView>(R.id.avatar5)
            val avatar6 = dialog.findViewById<ImageView>(R.id.avatar6)

            close.setOnClickListener {
                dialog.dismiss()
            }

            val lUserDetails = UserDetails.getInstance()

            avatar1.setOnClickListener {
                lUserDetails.profileAvatar = R.drawable.avatar1
                Glide.with(context)
                    .load(UserDetails.getInstance().profileAvatar)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.avatar1)
                    .into(view);
                dialog.dismiss()
            }

            avatar2.setOnClickListener {
                lUserDetails.profileAvatar = R.drawable.avatar2
                Glide.with(context)
                    .load(UserDetails.getInstance().profileAvatar)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.avatar2)
                    .into(view);
                dialog.dismiss()
            }

            avatar3.setOnClickListener {
                lUserDetails.profileAvatar = R.drawable.avatar3
                Glide.with(context)
                    .load(UserDetails.getInstance().profileAvatar)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.avatar3)
                    .into(view);
                dialog.dismiss()
            }

            avatar4.setOnClickListener {
                lUserDetails.profileAvatar = R.drawable.avatar4
                Glide.with(context)
                    .load(UserDetails.getInstance().profileAvatar)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.avatar4)
                    .into(view);
                dialog.dismiss()
            }

            avatar5.setOnClickListener {
                lUserDetails.profileAvatar = R.drawable.avatar5
                Glide.with(context)
                    .load(UserDetails.getInstance().profileAvatar)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.avatar5)
                    .into(view);
                dialog.dismiss()
            }
            avatar6.setOnClickListener {
                lUserDetails.profileAvatar = R.drawable.avatar6
                Glide.with(context)
                    .load(UserDetails.getInstance().profileAvatar)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.avatar6)
                    .into(view);
                dialog.dismiss()
            }

            dialog.show()
        } catch (e: Exception) {
            CoexclLogs.printException(e)
        }

    }
}