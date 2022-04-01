package com.home.coexcleducation.jdo;

import android.content.Context;
import android.content.SharedPreferences;

import com.home.coexcleducation.MainApplication;
import com.home.coexcleducation.R;
import com.home.coexcleducation.utils.PreferenceHelper;

public class UserDetails {

    private static UserDetails instance;
    private SharedPreferences mSharedpreference;
    private String LOGGED_IN = "loggedin";
    private String ID = "id";
    private String NAME = "name";
    private String MOBILE = "mobile";
    private String PROFILE_IMAGE = "profile_image";
    private String PROFILE_AVATAR = "profile_avatar";
    private String EMAIL = "email";
    private String IMAGE_URL = "image_url";
    private String SUBSCRIBED = "subscribed";
    private String GENDER = "gender";

    private String CLASS = "class";
    private String ROLL_NO = "rollNo";
    private String FAV_SUBJECT = "favouriteSubject";

    private String SCHOOL_NAME = "schoolName";
    private String SCHOOL_CODE = "schoolCode";
    private String CITY = "city";
    private String STATE = "state";

    private String FATHER_NAME = "fatherName";
    private String MOTHER_NAME = "motherName";
    private String PARENT_MOBILE = "parentMobile";
    private String BLOOD_GROUP = "bloodGroup";
    private String HOBBY = "hobby";
    private String FAV_SPORTS = "favouriteSport";

    private String PROGRESS_REPORT_PERCENTAGE = "progress_report";
    private String QUIZ_LAST_ATTEMPT = "quiz_last_attempt";


    private UserDetails(Context pContext) {
        mSharedpreference = PreferenceHelper.getSharedPreference(pContext);
    }

    public static synchronized UserDetails getInstance() {
        if (instance == null) {
            instance = new UserDetails(MainApplication.getContext());
        }
        return instance;
    }


    public void setLoggedIn(Boolean logged_in) {
        mSharedpreference.edit().putBoolean(LOGGED_IN , logged_in).apply();
    }

    public boolean isLoggedIn() {
        return mSharedpreference.getBoolean(LOGGED_IN , false);
    }

    public String getID() {
        return mSharedpreference.getString(ID , "");
    }

    public void setID(String id) {
        mSharedpreference.edit().putString(ID , id).apply();
    }

    public String getName() {
        return mSharedpreference.getString(NAME , "");
    }

    public void setName(String name) {
        mSharedpreference.edit().putString(NAME , name).apply();
    }

    public String getSchoolImageUrl() {
        return mSharedpreference.getString(IMAGE_URL , "");
    }

    public void setSchoolImageUrl(String imageUrl) {
        mSharedpreference.edit().putString(IMAGE_URL , imageUrl).apply();
    }

    public String getMobile() {
        return mSharedpreference.getString(MOBILE , "");
    }

    public void setMobile(String mobile) {
        mSharedpreference.edit().putString(MOBILE , mobile).apply();
    }
    public String getProfileImage() {
        return mSharedpreference.getString(PROFILE_IMAGE , "");
    }

    public void setProfileImage(String profile_image) {
        mSharedpreference.edit().putString(PROFILE_IMAGE , profile_image).apply();
    }

    public Integer getProfileAvatar() {
        return mSharedpreference.getInt(PROFILE_AVATAR , R.drawable.avatar1);
    }

    public void setProfileAvatar(Integer profile_avatar) {
        mSharedpreference.edit().putInt(PROFILE_AVATAR , profile_avatar).apply();
    }

    public String getEmail() {
        return mSharedpreference.getString(EMAIL , "");
    }

    public void setEmail(String email) {
        mSharedpreference.edit().putString(EMAIL , email).apply();
    }

    public boolean getSubscribed() {
        return mSharedpreference.getBoolean(SUBSCRIBED , false);
    }

    public void setSubscribed(Boolean subscribed) {
        mSharedpreference.edit().putBoolean(SUBSCRIBED , subscribed).apply();
    }


    public String getFatherName() {
        return mSharedpreference.getString(FATHER_NAME , "");
    }

    public void setFatherName(String fatherName) {
        mSharedpreference.edit().putString(FATHER_NAME , fatherName).apply();
    }

    public String getMotherName() {
        return mSharedpreference.getString(MOTHER_NAME , "");
    }

    public void setMotherName(String motherName) {
        mSharedpreference.edit().putString(MOTHER_NAME , motherName).apply();
    }

    public String getParentMobile() {
        return mSharedpreference.getString(PARENT_MOBILE , "");
    }

    public void setParentMobile(String mobile) {
        mSharedpreference.edit().putString(PARENT_MOBILE , mobile).apply();
    }

    public String getBloodGroup() {
        return mSharedpreference.getString(BLOOD_GROUP , "");
    }

    public void setBloodGroup(String bloodGroup) {
        mSharedpreference.edit().putString(BLOOD_GROUP , bloodGroup).apply();
    }

    public String getHobby() {
        return mSharedpreference.getString(HOBBY , "");
    }

    public void setHobby(String hobby) {
        mSharedpreference.edit().putString(HOBBY , hobby).apply();
    }

    public String getFavSports() {
        return mSharedpreference.getString(FAV_SPORTS , "");
    }

    public void setFavSports(String fav_sports) {
        mSharedpreference.edit().putString(FAV_SPORTS , fav_sports).apply();
    }



    public String getClassName() {
        return mSharedpreference.getString(CLASS , "");
    }

    public void setClassName(String className) {
        mSharedpreference.edit().putString(CLASS , className).apply();
    }

    public String getRollNo() {
        return mSharedpreference.getString(ROLL_NO , "");
    }

    public void setRollNo(String roll_no) {
        mSharedpreference.edit().putString(ROLL_NO , roll_no).apply();
    }

    public String getFavSubject() {
        return mSharedpreference.getString(FAV_SUBJECT , "");
    }

    public void setFavSubject(String favSubject) {
        mSharedpreference.edit().putString(FAV_SUBJECT , favSubject).apply();
    }


    public String getSchoolName() {
        return mSharedpreference.getString(SCHOOL_NAME , "");
    }

    public void setSchoolName(String schoolName) {
        mSharedpreference.edit().putString(SCHOOL_NAME , schoolName).apply();
    }


    public String getSchoolCode() {
        return mSharedpreference.getString(SCHOOL_CODE , "");
    }

    public void setSchoolCode(String schoolCode) {
        mSharedpreference.edit().putString(SCHOOL_CODE , schoolCode).apply();
    }

    public String getCity() {
        return mSharedpreference.getString(CITY , "");
    }

    public void setCity(String city) {
        mSharedpreference.edit().putString(CITY , city).apply();
    }

    public String getState() {
        return mSharedpreference.getString(STATE , "");
    }

    public void setState(String state) {
        mSharedpreference.edit().putString(STATE , state).apply();
    }

    public Float getProgressReport() {
        return mSharedpreference.getFloat(PROGRESS_REPORT_PERCENTAGE , 0.0F);
    }

    public void setProgressReport(Float progress) {
        mSharedpreference.edit().putFloat(PROGRESS_REPORT_PERCENTAGE , progress).apply();
    }

    public String getQuizLastAttempt() {
        return mSharedpreference.getString(QUIZ_LAST_ATTEMPT , "");
    }

    public void setQuizLastAttempt(String date) {
        mSharedpreference.edit().putString(QUIZ_LAST_ATTEMPT , date).apply();
    }

    public String getGender() {
        return mSharedpreference.getString(GENDER , "");
    }

    public void setGender(String gender) {
        mSharedpreference.edit().putString(GENDER , gender).apply();
    }
}
