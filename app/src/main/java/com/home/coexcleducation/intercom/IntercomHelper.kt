package com.home.coexcleducation.intercom

import android.app.Application
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.home.coexcleducation.MainApplication
import com.home.coexcleducation.R
import com.home.coexcleducation.jdo.UserDetails
import io.intercom.android.sdk.Company
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.UserAttributes
import io.intercom.android.sdk.identity.Registration

class IntercomHelper {

    fun startIntercomChat(pContext : Context) {
        Intercom.client().displayMessageComposer()
    }

    fun initialize(pContext : Application) {
        Intercom.initialize(
            pContext,
            MainApplication.CONTEXT.getString(R.string.intercom_chat_app_key),
            MainApplication.CONTEXT.getString(R.string.intercom_chat_app_id)
        )
    }

    fun regsisterIntercomUser(pUserID: String) {
        Intercom.client().registerIdentifiedUser(Registration().withUserId(pUserID))
    }

    fun unRegsisterIntercomUser() {
        Intercom.client().logout()
    }

     fun updateUser(pContext : Context) {

         var lUserDetail = UserDetails.getInstance()
         val company = Company.Builder()
             .withCompanyId(lUserDetail.schoolName)
             .build()

         val lDevicemap: MutableMap<String, Any?> = HashMap()
         lDevicemap["DeviceJDO ID"] =
             Settings.Secure.getString(
                 MainApplication.getContext().getContentResolver(),
                 Settings.Secure.ANDROID_ID
             )
         lDevicemap["DeviceJDO"] = Build.DEVICE
         lDevicemap["Model"] = Build.MODEL
         lDevicemap["Product"] = Build.PRODUCT
         lDevicemap["Android Sdk Release"] = Build.VERSION.RELEASE
         lDevicemap["Android Sdk Version"] = Build.VERSION.SDK_INT
         val userAttributes = UserAttributes.Builder()
             .withCompany(company)
             .withName(lUserDetail.name)
             .withUserId(lUserDetail.id)
             .withEmail(lUserDetail.email)
             .withCustomAttribute("status", "Active")
             .withCustomAttributes(lDevicemap)
             .withCustomAttribute("From app", "Coexcl Android")
             .withCustomAttribute(
                 "appVersion",
                 pContext.packageManager.getPackageInfo(pContext.packageName, 0).versionCode
             )
             .build()
         Intercom.client().updateUser(userAttributes)
        }
}