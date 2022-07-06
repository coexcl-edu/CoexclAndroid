package com.home.coexcleducation.premium

import android.content.Context
import android.util.Log
import com.home.coexcleducation.httphelper.HttpClient
import com.home.coexcleducation.httphelper.HttpContentType
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.httphelper.HttpMethod
import com.home.coexcleducation.utils.ApiConstant

class PremiumAPI(context: Context) {

    fun upgradeToPremium(lHttpHelper : HttpHelper, context: Context) : HttpHelper {
         try {
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.POST.name
            lHttpHelper.url = ApiConstant.PREMIUM_UPGRADE_API
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lHttpHelper
    }

    fun downgradePlan(lHttpHelper : HttpHelper, context: Context) : HttpHelper {
        try {
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.POST.name
            lHttpHelper.url = ApiConstant.PREMIUM_DOWN_UPGRADE_API
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lHttpHelper
    }

    fun getPlanInfo(lHttpHelper : HttpHelper, context: Context) : HttpHelper {
        try {
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.POST.name
            lHttpHelper.url = ApiConstant.GET_PLAN_API
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lHttpHelper
    }

    fun updatePlanStatus(lHttpHelper : HttpHelper, context: Context) : HttpHelper {
        try {
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.POST.name
            lHttpHelper.url = ApiConstant.UPDATE_PLAN_STATUS_API
            Log.e("TAG", "getUpdatePlanStatus - "+lHttpHelper.url)
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lHttpHelper
    }

}