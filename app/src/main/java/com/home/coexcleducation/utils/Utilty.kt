package com.home.coexcleducation.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.coexcleducation.MainApplication
import com.home.coexcleducation.R
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.intercom.IntercomHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.premium.UpgradePlanActivity
import com.home.coexcleducation.ui.registration.SignUpUtils
import java.io.StringWriter
import java.io.Writer

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
                    var lSessionInfo: HashMap<String, Any> = if(map.containsKey("limitedSession"))
                        map.get("limitedSession") as HashMap<String, Any> else HashMap<String, Any>()


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
                    lUserDetails.fcmToken =
                        if (map.containsKey("fcmtoken")) map.get("fcmtoken").toString() else ""
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


                    lUserDetails.sessionDate = if (lSessionInfo.containsKey("date")) lSessionInfo["date"].toString()
                    else Helper().currentMonth

                    lUserDetails.sessionCount = (if (lSessionInfo.containsKey("session")) lSessionInfo["session"]
                    else 0) as Int?


                    lUserDetails.isLoggedIn = true
                    IntercomHelper().regsisterIntercomUser(lUserDetails.id)
                    IntercomHelper().updateUser(MainApplication.CONTEXT)
                }
            }
        }
    }

    @SuppressLint("Range")
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

    fun updateToken(pContext : Context, refreshedToken : String) :HttpHelper {
        val lWriter: Writer = StringWriter()
        val mReqMap: java.util.HashMap<String, Any> = hashMapOf()
        mReqMap["userid"] = UserDetails.getInstance().id
        mReqMap["fcmtoken"] = refreshedToken
        ObjectMapper().writeValue(lWriter, mReqMap)

        var lHttpHelper = HttpHelper()
        lHttpHelper.payload = lWriter.toString()
        CoexclLogs.errorLog("MainActivity", "Req from Update Token - " + lHttpHelper.payload)
        lHttpHelper = SignUpUtils().updateToken(pContext, lHttpHelper)
        CoexclLogs.errorLog("MainActivity", "Response from  Update Token - " + lHttpHelper.response)
        var result = lHttpHelper.payload
        try {
            if (!result.isNullOrEmpty()) {
                var lMapper = ObjectMapper()
                var lResponseObject = lMapper.readValue(result, java.util.HashMap::class.java)
                if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                    var data = lResponseObject.get("data") as HashMap<String, Any>
                    UserDetails.getInstance().fcmToken =
                        if (data.containsKey("fcmtoken")) data.get("fcmtoken").toString() else ""
                }
            }
        } catch (e : java.lang.Exception) {

        }
        return lHttpHelper
    }


    fun displayonSessionOver(context: Activity) {
        try {
            val alertConfirm = AlertDialog.Builder(context)
            alertConfirm.setTitle("Free Session Over")
            alertConfirm.setMessage("You have exhausted all your free session for this month, To get unlimited session by buying our premium plan")
            alertConfirm.setCancelable(true)
            alertConfirm.setNegativeButton("Okay") { dialog, which ->
                dialog.dismiss()
            }
            alertConfirm.setPositiveButton("Buy Premium") { dialog, which ->
                FirebaseAnalyticsCoexcl().logFirebaseEvent(context, "", "Home", "Seesion_Premium_Screen")
                dialog.dismiss()
                context.startActivity(Intent(context, UpgradePlanActivity::class.java))
                context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            }
            val dialog = alertConfirm.create()
            dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun updateSessionCount(context: Context) {
        try {
            var response = SignUpUtils().updateLimitedSession(context).response
            CoexclLogs.errorLog("TAG", "get Session Response - $response")
            if (!response.isNullOrEmpty()) {
                var lMapper = ObjectMapper()
                var lResponseObject = lMapper.readValue(response, HashMap::class.java)
                if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                    var data = lResponseObject.get("data") as HashMap<String, Any>
                    if (data.containsKey("sessionCount")) {
                        UserDetails.getInstance().sessionCount = data.get("sessionCount") as Int?;
                        CoexclLogs.errorLog("TAG", "get Session count - $response")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}