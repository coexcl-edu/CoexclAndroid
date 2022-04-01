package com.home.coexcleducation.ui.videos

import android.content.Context
import com.home.coexcleducation.httphelper.HttpClient
import com.home.coexcleducation.httphelper.HttpContentType
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.httphelper.HttpMethod
import com.home.coexcleducation.jdo.UserDetails
import com.home.coexcleducation.utils.ApiConstant

class VideoUtils {
    fun fetchVideo(context: Context, payload : String): HttpHelper? {
        return try {
            val lHttpHelper = HttpHelper()
            lHttpHelper.payload = payload
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.POST.name
            lHttpHelper.url = ApiConstant.VIDEO_FETCH_API
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}