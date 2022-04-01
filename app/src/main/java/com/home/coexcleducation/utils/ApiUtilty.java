package com.home.coexcleducation.utils;

import android.content.Context;

import com.home.coexcleducation.httphelper.HttpClient;
import com.home.coexcleducation.httphelper.HttpContentType;
import com.home.coexcleducation.httphelper.HttpHelper;
import com.home.coexcleducation.httphelper.HttpMethod;

import java.io.File;

public class ApiUtilty {

    public HttpHelper fetchLeaderBoard(Context context) {
        try {
            HttpHelper lHttpHelper = new HttpHelper();
            lHttpHelper.setRequestType(HttpMethod.GET.name());
            lHttpHelper.setUrl(ApiConstant.LEADER_BOARD_API+"/10");
            CoexclLogs.errorLog("LederBorad", "lReq - "+lHttpHelper.getUrl());
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpHelper uploadImage(Context context, File pFile) {
        try {
            HttpHelper pHttpHelper = new HttpHelper();
            pHttpHelper.setUrl(ApiConstant.IMAGE_UPLOAD_API);
            pHttpHelper.setRequestType(HttpMethod.POST.name());
            pHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            return new HttpClient(context).fileUploader(pFile, pHttpHelper, context);
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
        return null;
    }

    public HttpHelper fetchQuiz(Context context, String lPayLoad) {
        try {
            HttpHelper pHttpHelper = new HttpHelper();
            pHttpHelper.setUrl(ApiConstant.QUIZ_API);
            pHttpHelper.setPayload(lPayLoad);
            pHttpHelper.setRequestType(HttpMethod.POST.name());
            pHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            return new HttpClient(context).executeHttpRequest(pHttpHelper);
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
        return null;
    }


    public HttpHelper fetchFunFacts(Context context, String lPayLoad) {
        try {
            HttpHelper pHttpHelper = new HttpHelper();
            pHttpHelper.setUrl(ApiConstant.FUN_FACT_API);
            pHttpHelper.setPayload(lPayLoad);
            pHttpHelper.setRequestType(HttpMethod.POST.name());
            pHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            return new HttpClient(context).executeHttpRequest(pHttpHelper);
        } catch (Exception e) {
            CoexclLogs.printException(e);
        }
        return null;
    }


}
