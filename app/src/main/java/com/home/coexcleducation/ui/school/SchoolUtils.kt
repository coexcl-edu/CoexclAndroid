package com.home.coexcleducation.ui.school

import android.content.Context
import com.home.coexcleducation.httphelper.HttpClient
import com.home.coexcleducation.httphelper.HttpContentType
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.httphelper.HttpMethod
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.ApiConstant

class SchoolUtils {

    fun fetchActivity(context: Context): HttpHelper? {
        return try {
            val lHttpHelper = HttpHelper()
            lHttpHelper.requestType = HttpMethod.GET.name
            lHttpHelper.url = ApiConstant.NOTICE_API+UserDetails.getInstance().id
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun fetchLiveClass(context: Context, payload : String): HttpHelper? {
        return try {
            val lHttpHelper = HttpHelper()
            lHttpHelper.payload = payload
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.POST.name
            lHttpHelper.url = ApiConstant.LIVE_CLASSES_FETCH_API
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}