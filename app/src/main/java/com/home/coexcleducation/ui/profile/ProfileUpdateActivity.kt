package com.home.coexcleducation.ui.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.ObjectMapper
import com.freshchat.consumer.sdk.Freshchat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.home.coexcleducation.R
import com.home.coexcleducation.database.NotificationTable
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.ui.image.ImagePicker
import com.home.coexcleducation.ui.image.OnImagePickerListener
import com.home.coexcleducation.ui.registration.LoginActivity
import com.home.coexcleducation.ui.registration.SignUpUtils
import com.home.coexcleducation.utils.*
import kotlinx.android.synthetic.main.bottom_sheet_signup.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.school_id
import kotlinx.android.synthetic.main.profile_update_view.*
import java.io.File
import java.io.StringWriter
import java.io.Writer
import java.util.*


class ProfileUpdateActivity : AppCompatActivity() {

    var TAG = "ProfileUpdateActivity"
    var mFavSubject = ""
    var mGender = ""
    var mBloodGroup = ""
    var mClass = ""
    var mName = ""
    var mAim = ""
    var mEmail = ""
    var mFavSports = ""
    var mFatherName = ""
    var mMotherName = ""
    var mParentMobile = ""
    lateinit var dialog : Dialog
    var mContactImageShown = false
    var mContactImageRemove = false
    var mPhotoTaken = false
    var mTakenFromCamera = false
    var PICK_FROM_FILE = 1
    val CROP_FROM_CAMERA = 2
    val PICK_FROM_CAMERA = 3
    var picUri: Uri? = null
    var mCropUri : Uri? = null
    lateinit var mContext : Context
    lateinit var imagePicker : ImagePicker
    lateinit var mChooseSchoolDialog: BottomSheetDialog
    lateinit var mDialogView: View
    lateinit var mDialogSchoolID : String
    lateinit var mSchoolID : String
    lateinit var mSchoolDetailsTV : TextView
    lateinit var mSchoolErrorTV : TextView
    lateinit var mSchoolName : String
    lateinit var mCity : String
    lateinit var mState : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_update_view)

        mContext = this
        imagePicker = ImagePicker(mContext)

        mChooseSchoolDialog= BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        mDialogView= layoutInflater.inflate(R.layout.bottom_sheet_signup,null)
        mChooseSchoolDialog.setContentView(mDialogView)

        mSchoolDetailsTV = findViewById(R.id.school_details)
        mSchoolErrorTV = mDialogView.findViewById(R.id.school_error)


        close.setOnClickListener{
            checkForDataChanges()
        }

        front_profile.setOnClickListener{
//            startPicIntent()
            Utilty().getProfileAvatar(mContext, front_profile)
        }

        choose_school.setOnClickListener{
            mChooseSchoolDialog.show()
        }

        mDialogView.find_School.setOnClickListener{
            mDialogSchoolID = mDialogView.school_id.text.toString()
            if(!mDialogSchoolID.isNullOrEmpty()) {
                if(mDialogSchoolID.length >= 4) {
                    FindSchoolCode().execute()
                } else {
                    ViewUtils().displayToast("Please provide valid School code", "failure", this, "")
                }
            } else {
                ViewUtils().displayToast("Please provide your School code", "failure", this, "")
            }
        }

        save.setOnClickListener{
            mName = name.text.toString()
            if(!mName.isNullOrEmpty()) {
                name.error = null
                mEmail = email.text.toString()
                mAim = aim.text.toString()
                mFavSports = fav_sport.text.toString()
                mFatherName = parent_name.text.toString()
                mMotherName = mother_name.text.toString()
                mParentMobile = parent_phone.text.toString()

                if(!mEmail.isNullOrEmpty()){
                    if(!Helper().isValidEmailID(mEmail)) {
                        ViewUtils().displayToast("Invalid email address", "failure", this@ProfileUpdateActivity, "")
                        email.error = "Invalid email address"
                        return@setOnClickListener
                    }
                }


                if(!mParentMobile.isNullOrEmpty()) {
                    if (mParentMobile.length != 10) {
                        ViewUtils().displayToast("Invalid parent's mobile", "failure", this@ProfileUpdateActivity, "")
                        parent_phone.error = "Invalid parent's mobile"
                        return@setOnClickListener
                    } else {
                        parent_phone.error = null
                    }
                }

                ProfileUpdate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            } else {
                name.error = "Name must not be empty"
            }
        }


        fav_subject.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mFavSubject = if(fav_subject.selectedItem.toString() == "Choose your favorite subject")
                    ""
                else
                    fav_subject.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        blood_group.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mBloodGroup = if(blood_group.selectedItem.toString() == "Choose your blood group")
                    ""
                else
                    blood_group.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        gender.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mGender = if(gender.selectedItem.toString() == "Choose your gender")
                    ""
                else
                    gender.selectedItem.toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        class_spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(class_spinner.selectedItem.toString() == "Class 6")
                    mClass = "6"
                else if(class_spinner.selectedItem.toString() == "Class 7")
                    mClass = "7"
                else if(class_spinner.selectedItem.toString() == "Class 8")
                    mClass = "8"
                else if(class_spinner.selectedItem.toString() == "Class 9")
                    mClass = "9"
                else if(class_spinner.selectedItem.toString() == "Class 10")
                    mClass = "10"
                else if(class_spinner.selectedItem.toString() == "Class 10+1")
                    mClass = "11"
                else if(class_spinner.selectedItem.toString() == "Class 10+2")
                    mClass = "12"

                CoexclLogs.errorLog(TAG, "mClass -- " + mClass)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@ProfileUpdateActivity, "", "Home", "ProfileUpdateView")
        setAdapters()
        setValue()
    }

    override fun onBackPressed() {
        checkForDataChanges()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setValue() {
        var lUserDetail = UserDetails.getInstance()
        name.setText(lUserDetail.name)
        email.setText(lUserDetail.email)
        fav_sport.setText(lUserDetail.favSports)
        aim.setText(lUserDetail.hobby)
        parent_name.setText(lUserDetail.fatherName)
        mother_name.setText(lUserDetail.motherName)
        parent_phone.setText(lUserDetail.parentMobile)
        mSchoolID = lUserDetail.schoolCode
        mSchoolName = lUserDetail.schoolName
        mCity = lUserDetail.city
        mState = lUserDetail.state
        school_details.text = mSchoolName + ", " +  mCity
    }

    private fun checkForDataChanges() {
        var lUserDetail = UserDetails.getInstance()
        CoexclLogs.errorLog(TAG, name.text.toString() +"===== "+lUserDetail.name)
        CoexclLogs.errorLog(TAG, email.text.toString() +"===== "+lUserDetail.email)
        CoexclLogs.errorLog(TAG, mother_name.text.toString() +"===== "+lUserDetail.motherName)
        CoexclLogs.errorLog(TAG, parent_name.text.toString() +"===== "+lUserDetail.fatherName)
        CoexclLogs.errorLog(TAG, parent_phone.text.toString() +"===== "+lUserDetail.parentMobile)
        CoexclLogs.errorLog(TAG, aim.text.toString() +"===== "+lUserDetail.hobby)
        CoexclLogs.errorLog(TAG, fav_sport.text.toString() +"===== "+lUserDetail.favSports)
        CoexclLogs.errorLog(TAG, mName +"===== "+lUserDetail.name)
        CoexclLogs.errorLog(TAG, mName +"===== "+lUserDetail.name)
        CoexclLogs.errorLog(TAG, "$mSchoolID ===== "+lUserDetail.schoolCode)
        CoexclLogs.errorLog(TAG, "$mFavSubject ===== "+lUserDetail.favSubject)
        CoexclLogs.errorLog(TAG, "$mGender ===== "+lUserDetail.gender)
        CoexclLogs.errorLog(TAG, "$mBloodGroup ===== "+lUserDetail.bloodGroup)

        if(name.text.toString() == lUserDetail.name
            && email.text.toString() == lUserDetail.email
            && mother_name.text.toString() == lUserDetail.motherName
            && parent_name.text.toString() == lUserDetail.fatherName
            && parent_phone.text.toString() == lUserDetail.parentMobile
            && aim.text.toString() == lUserDetail.hobby
            && fav_sport.text.toString() == lUserDetail.favSports
            && mSchoolID == lUserDetail.schoolCode
            && mFavSubject == lUserDetail.favSubject
            && mGender == lUserDetail.gender
            && mBloodGroup == lUserDetail.bloodGroup) {
            exit()
        } else {
            displayAlertDialogOnDataNotSaved()
        }
    }

    private fun setAdapters() {
        val myGenderList = resources.getStringArray(R.array.gender)
        var mAdapter = ArrayAdapter(this, R.layout.spinner_item_layout, myGenderList.toList())
        gender.adapter = mAdapter
        var mGenderCount = if (myGenderList.indexOf(UserDetails.getInstance().gender) != -1) myGenderList.indexOf(
            UserDetails.getInstance().gender
        ) else 0
        gender.setSelection(mGenderCount)

        val mClassList = resources.getStringArray(R.array.class_name)
        mAdapter = ArrayAdapter(this, R.layout.spinner_item_layout, mClassList.toList())
        class_spinner.adapter = mAdapter
        var mClassCount = 0

        if(UserDetails.getInstance().className == "6")
            mClassCount = 0
        else if(UserDetails.getInstance().className == "7")
            mClassCount = 1
        else if(UserDetails.getInstance().className == "8")
            mClassCount = 2
        else if(UserDetails.getInstance().className == "9")
            mClassCount = 3
        else if(UserDetails.getInstance().className == "10")
            mClassCount = 4
        else if(UserDetails.getInstance().className == "11")
            mClassCount = 5
        else if(UserDetails.getInstance().className == "12")
            mClassCount = 6

        CoexclLogs.errorLog(TAG, "Class Name - "+UserDetails.getInstance().className)
        CoexclLogs.errorLog(TAG, "Class Name - "+mClassCount)

        class_spinner.setSelection(mClassCount)

        val myBloodGroupList = resources.getStringArray(R.array.blood_group)
        mAdapter = ArrayAdapter(this, R.layout.spinner_item_layout, myBloodGroupList.toList())
        blood_group.adapter = mAdapter
        var mBloodCount = if (myBloodGroupList.indexOf(UserDetails.getInstance().bloodGroup) != -1) myBloodGroupList.indexOf(
            UserDetails.getInstance().bloodGroup
        ) else 0
        blood_group.setSelection(mBloodCount)

        val myFavSubjectList = resources.getStringArray(R.array.fav_subject)
        mAdapter = ArrayAdapter(this, R.layout.spinner_item_layout, myFavSubjectList.toList())
        fav_subject.adapter = mAdapter
        var mFavSubCount = if (myFavSubjectList.indexOf(UserDetails.getInstance().favSubject) != -1) myFavSubjectList.indexOf(
            UserDetails.getInstance().favSubject
        ) else 0
        fav_subject.setSelection(mFavSubCount)


        CoexclLogs.errorLog("TAG", "Profile -- " + UserDetails.getInstance().profileImage)
//        var imageUrl = UserDetails.getInstance().profileImage
//        front_profile.setImageResource(UserDetails.getInstance().profileAvatar)
        Glide.with(this)
                .load(UserDetails.getInstance().profileAvatar)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.avatar2)
                .into(front_profile);
    }

    private fun constructParams() : String {
        try {
            val lWriter: Writer = StringWriter()
            val mReqMap: HashMap<String, Any> = hashMapOf()
            mReqMap["name"] = mName
            mReqMap["email"] = mEmail
            mReqMap["userid"] = UserDetails.getInstance().id

                val mPersonalMap: HashMap<String, Any> = hashMapOf()
                mPersonalMap["fatherName"] = mFatherName
                mPersonalMap["motherName"] = mMotherName
                mPersonalMap["parentContact"] = mParentMobile
                mPersonalMap["gender"] = mGender
                mPersonalMap["hobby"] = mAim
                mPersonalMap["favouriteSport"] = mFavSports
                mPersonalMap["bloodGroup"] = mBloodGroup

                val mAcademicsMap: HashMap<String, Any> = hashMapOf()
                mAcademicsMap["class"] = mClass
                mAcademicsMap["favouriteSubject"] = mFavSubject

                val mSchoolsMap: HashMap<String, Any> = hashMapOf()

//                if(!mSchoolID.isNullOrEmpty()) {
//                    mSchoolsMap["schoolName"] = mSchoolName
//                    mSchoolsMap["schoolCode"] = mSchoolID
//                    mSchoolsMap["city"] = mCity
//                    mSchoolsMap["state"] = mState
//                } else {
//                    mSchoolsMap["schoolName"] = "Coexcl"
//                    mSchoolsMap["schoolCode"] = "COX2021001"
//                    mSchoolsMap["city"] = "Bihar"
//                    mSchoolsMap["state"] = "India"
//                }

            if(!mSchoolID.isNullOrEmpty()) {
                mSchoolsMap["schoolName"] = mSchoolName
                mSchoolsMap["schoolCode"] = mSchoolID
                mSchoolsMap["city"] = mCity
                mSchoolsMap["state"] = mState
                mReqMap["subscribed"] = true
            } else {
                mSchoolsMap["schoolName"] = "Coexcl"
                mSchoolsMap["schoolCode"] = "COX2021001"
                mSchoolsMap["city"] = "Bihar"
                mSchoolsMap["state"] = "India"
                mReqMap["subscribed"] = false
            }
            
            mReqMap["schoolInfo"] = mSchoolsMap
            mReqMap["personalInfo"] = mPersonalMap
            mReqMap["academics"] = mAcademicsMap

//            if (mContactImageRemove) {
//                mReqMap.put("profileimgurl", "")
//            } else {
//                if (mPhotoTaken) {
//                    CoexclLogs.errorLog(TAG, "lImageResponse req")
//                    val lImageResponse: String = ApiUtilty().uploadImage(mContext, imagePicker.tempFile).getResponse()
//                    CoexclLogs.errorLog(TAG, "lImageResponse "+lImageResponse.toString())
//                    var lDataMap = ObjectMapper().readValue<HashMap<*, *>>(lImageResponse, HashMap::class.java)
//                    var lImageMap = lDataMap.get("data") as HashMap<String?, Any?>
//                }
//            }

            ObjectMapper().writeValue(lWriter, mReqMap)
            return lWriter.toString()
        } catch (e: java.lang.Exception) {
            CoexclLogs.printException(e)
            return ""
        }
    }

    private fun exit() {
        ViewUtils().exitActivityToRight(this)
        finish()
    }


    inner class ProfileUpdate : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ViewUtils().displayProgressDialog(
                "Updating Profile...",
                this@ProfileUpdateActivity
            )
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var lHttpHelper = HttpHelper()
                lHttpHelper.payload = constructParams()
                CoexclLogs.errorLog(TAG, "Req from Profile Update - " + lHttpHelper.payload)
                lHttpHelper = SignUpUtils().updateProfile(this@ProfileUpdateActivity, lHttpHelper)
                CoexclLogs.errorLog(TAG, "Response Profile Update - " + lHttpHelper.response)
                return lHttpHelper.response
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(dialog.isShowing)
                dialog.dismiss()
            try {
                if (!result.isNullOrEmpty()) {
                    var lMapper = ObjectMapper()
                    var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                    if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                        Utilty().insertLoginData(result)
                        try {
                            val freshchatUser = Freshchat.getInstance(applicationContext).user
                            var lUserDetails = UserDetails.getInstance()
                            freshchatUser.firstName = lUserDetails.name
                            freshchatUser.email = lUserDetails.email
                            freshchatUser.setPhone("+91", lUserDetails.mobile)
                        } catch (e :Exception) {
                            e.printStackTrace()
                        }
                        exit()
                        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@ProfileUpdateActivity, "", "Home", "ProfileUpdate Success")
                    } else {
                        ViewUtils().displayToast(
                            "Profile Update Failed",
                            "failure",
                            this@ProfileUpdateActivity,
                            ""
                        )
                        FirebaseAnalyticsCoexcl().logFirebaseEvent(this@ProfileUpdateActivity, "", "Home", "ProfileUpdate Fail")
                    }
                } else {
                    ViewUtils().displayToast(
                        "Something went wrong",
                        "failure",
                        this@ProfileUpdateActivity,
                        ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun launchCamera() {
        mTakenFromCamera = true
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            picUri = FileProvider.getUriForFile(
                this, applicationContext.packageName + ".provider",
                File(
                    Environment.getExternalStorageDirectory(),
                    "tmp_avatar_" + System.currentTimeMillis() + ".jpg"
                )
            )
        } else picUri = Uri.fromFile(
            File(
                Environment.getExternalStorageDirectory(),
                "tmp_avatar_" + System.currentTimeMillis() + ".jpg"
            )
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri)
        try {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.putExtra("return-data", true)
            startActivityForResult(intent, PICK_FROM_CAMERA)
        } catch (e: ActivityNotFoundException) {
            CoexclLogs.printException(e)
        }
    }


    fun startPicIntent() {
        imagePicker.pickImage(mContactImageShown, object : OnImagePickerListener {
            override fun onImagepicked(uri: Uri?) {}
            override fun selectedAction(action: String?) {
                CoexclLogs.errorLog(TAG, "Action came Permission missing gallary")
                when (action) {
                    "remove" -> {
                        imagePicker.clearTempFile()
                        mContactImageRemove = true
                        mContactImageShown = false
                        front_profile.setImageDrawable(
                            ContextCompat.getDrawable(
                                mContext,
                                R.drawable.avatar1
                            )
                        )
                    }
                    "gallery" -> {
                        imagePicker.clearTempFile()
                        if (imagePicker.isImageAccessPermissionAcquired()) {
                            CoexclLogs.errorLog(TAG, " No Permission missing gallary")
                            val intent = Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(
                                intent,
                                PICK_FROM_FILE
                            )
                        } else {
                            CoexclLogs.errorLog(TAG, "Permission missing gallary")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(
                                    this@ProfileUpdateActivity, arrayOf(
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ), 2
                                )
                            }
                        }
                    }
                    "camera" -> {
                        imagePicker.clearTempFile()

                        if (imagePicker.isCameraAccesPermissionAcquired()) {
                            CoexclLogs.errorLog(TAG, "No Permission missing camera")
                            startActivityForResult(
                                imagePicker.getIntenttoOpenCamera(),
                                PICK_FROM_CAMERA
                            )
                        } else {
                            CoexclLogs.errorLog(TAG, "Permission missing camera")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(
                                    this@ProfileUpdateActivity, arrayOf(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ), 2
                                )
                            }
                        }
                    }
                    "cameranotavailable" -> startActivityForResult(
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        ), PICK_FROM_FILE
                    )
                }
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try{
            if (resultCode != RESULT_OK) return;

//            try {
//                val byteArrayOutputStreamObject: ByteArrayOutputStream
//
//                byteArrayOutputStreamObject = ByteArrayOutputStream()
//
//                // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
//
//                // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
//                var  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data!!.data);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject)
//
//                val byteArrayVar: ByteArray = byteArrayOutputStreamObject.toByteArray()
//
//                val ConvertImage: String = Base64.encodeToString(byteArrayVar, Base64.DEFAULT)
//                CoexclLogs.errorLog(TAG, "ConvertImage - "+ConvertImage)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }


            when (requestCode) {
                PICK_FROM_CAMERA -> if (!imagePicker.isImageCroppingAppAvailabe()) {
                    mCropUri = imagePicker.cropImageManually(
                        imagePicker.getImageTakefromCamera(),
                        true
                    )
                    grabImage(mCropUri, true)
                } else {
                    try {
                        startActivityForResult(
                            imagePicker.getIntentToCropImage(imagePicker.getImageTakefromCamera()),
                            CROP_FROM_CAMERA
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        ViewUtils().displayToast(
                            "This device doesn't support the crop action!",
                            "failure",
                            mContext as Activity,
                            ""
                        )
                    }
                }
                PICK_FROM_FILE -> {
                    CoexclLogs.errorLog(TAG, "Image came from gallary")
                    val selectedImage: Uri =
                        imagePicker.readSelectedFileAndWriteinOurReference(data!!.data)
                    if (!imagePicker.isImageCroppingAppAvailabe()) {
                        mCropUri = imagePicker.cropImageManually(data!!.data, false)
                        grabImage(mCropUri, true)
                    } else {
                        try {
                            startActivityForResult(
                                imagePicker.getIntentToCropImage(selectedImage),
                                CROP_FROM_CAMERA
                            )
                        } catch (e: java.lang.Exception) {
                            CoexclLogs.printException(e)
                            ViewUtils().displayToast(
                                "This device doesn't support the crop action!",
                                "failure",
                                mContext as Activity,
                                ""
                            )
                        }
                    }
                }
                CROP_FROM_CAMERA -> {
                    mCropUri = data!!.data
                    try {
                        grabImage(mCropUri, true)
                    } catch (e: java.lang.Exception) {
                        CoexclLogs.printException(e)
                    }
                    mTakenFromCamera = false
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun grabImage(mContactBitmap: Uri?, onCreateNew: Boolean) {
        try {
            if (mContactBitmap != null) {
                mPhotoTaken = true
//                Glide.with(this)
//                    .load(mContactBitmap)
//                    .centerCrop()
//                    .circleCrop()
//                    .placeholder(R.drawable.kid_place_holder)
//                    .into(front_profile);
                mContactImageShown = true
                mContactImageRemove = false

            }
        } catch (e: java.lang.Exception) {
            CoexclLogs.printException(e)
        }
    }


    inner class FindSchoolCode : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ViewUtils().displayProgressDialog("Finding Your School...", this@ProfileUpdateActivity)
        }

        override fun doInBackground(vararg params: String?): String {
            var lHttpHelper = SignUpUtils().findSchool(this@ProfileUpdateActivity, mDialogSchoolID)
            CoexclLogs.errorLog(TAG, "Req from Signup - " + lHttpHelper.payload)
            CoexclLogs.errorLog(TAG, "Response from Signup - " + lHttpHelper.response)
            return lHttpHelper.response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(dialog.isShowing)
                dialog.dismiss()
            try {
                if (!result.isNullOrEmpty()) {
                    var lMapper = ObjectMapper()
                    var lResponseObject = lMapper.readValue(result, HashMap::class.java)
                    if (lResponseObject.containsKey("response") && lResponseObject.get("response") as Boolean) {
                        var data = lResponseObject.get("data") as HashMap<String, Any>
                        if (data.containsKey("result")) {
                            var result = data.get("result") as HashMap<String, String>
                            mSchoolName = if(result.containsKey("schoolname")) result["schoolname"] as String else ""
                            mSchoolID = mDialogSchoolID
                            mCity = if(result.containsKey("city")) result["city"] as String else ""
                            mState = if(result.containsKey("state")) result["state"] as String else ""
                            mSchoolDetailsTV.text = mSchoolName+", "+mCity
                            mSchoolErrorTV.visibility = View.GONE
                            FirebaseAnalyticsCoexcl().logFirebaseEvent(this@ProfileUpdateActivity, "", "Profile", "School_fetch_by_code success")
                            if(mChooseSchoolDialog.isShowing)
                                mChooseSchoolDialog.dismiss()
                        } else {
                            mSchoolErrorTV.visibility = View.VISIBLE
                            mSchoolErrorTV.text = "No school found"
                            FirebaseAnalyticsCoexcl().logFirebaseEvent(this@ProfileUpdateActivity, "", "Profile", "School_fetch_by_code not_found")
                        }
                    } else {
                        ViewUtils().displayToast("Something went wrong", "failure", this@ProfileUpdateActivity, "")
                    }
                } else {
                    ViewUtils().displayToast("Something went wrong", "failure", this@ProfileUpdateActivity, "")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }



    fun displayAlertDialogOnDataNotSaved() {
        try {
            val dialog = Dialog(this, R.style.DialogCustomTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_popup)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val window = dialog.window
            window?.setGravity(Gravity.CENTER)
            window?.setLayout((getWindow().peekDecorView().width * 0.85).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            val lAlterMessage = dialog.findViewById<TextView>(R.id.Alert_Message)
            val leftBtnText = dialog.findViewById<TextView>(R.id.Dialog_LeftBtn)
            val rightBtnText = dialog.findViewById<TextView>(R.id.Dialog_RightBtn)
            val msg = dialog.findViewById<TextView>(R.id.Alert_Title)
            val lYesButton = dialog.findViewById<LinearLayout>(R.id.ConfirmLayout)
            val lNoButton = dialog.findViewById<LinearLayout>(R.id.CancelLayout)
            lAlterMessage.text = "Data not saved! Please save the changes? "
            leftBtnText.text = "Save"
            rightBtnText.text = "Discard"
            msg.text = "Save"
            lNoButton.setOnClickListener {
                dialog.dismiss()
                save.performClick()
            }
            lYesButton.setOnClickListener {
                dialog.dismiss()
                exit()
            }
            dialog.show()
        } catch (e: Exception) {
            CoexclLogs.printException(e);
        }
    }



}