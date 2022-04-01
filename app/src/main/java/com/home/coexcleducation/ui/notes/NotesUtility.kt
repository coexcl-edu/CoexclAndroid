package com.home.coexcleducation.ui.notes

import android.content.Context
import com.home.coexcleducation.httphelper.HttpClient
import com.home.coexcleducation.httphelper.HttpContentType
import com.home.coexcleducation.httphelper.HttpHelper
import com.home.coexcleducation.httphelper.HttpMethod
import com.home.coexcleducation.utils.ApiConstant

class NotesUtility {

    fun fetchNotes(context: Context, params : String): HttpHelper? {
        return try {
            val lHttpHelper = HttpHelper()
            lHttpHelper.payload = params
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.POST.name
            lHttpHelper.url = ApiConstant.NOTES_FETCH_API
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updateNotes(context: Context, params : String): HttpHelper? {
        return try {
            val lHttpHelper = HttpHelper()
            lHttpHelper.payload = params
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.PUT.name
            lHttpHelper.url = ApiConstant.NOTES_FETCH_API
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun addNotes(context: Context, params : String): HttpHelper? {
        return try {
            val lHttpHelper = HttpHelper()
            lHttpHelper.payload = params
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.POST.name
            lHttpHelper.url = ApiConstant.ADD_NOTES_API
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteNotes(context: Context, noteId : String): HttpHelper? {
        return try {
            val lHttpHelper = HttpHelper()
            lHttpHelper.contentType = HttpContentType.HTTP_CONTENTTYPE_JSON
            lHttpHelper.requestType = HttpMethod.DELETE.name
            lHttpHelper.url = ApiConstant.NOTES_FETCH_API+"/"+noteId
            HttpClient(context).executeHttpRequest(lHttpHelper)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}