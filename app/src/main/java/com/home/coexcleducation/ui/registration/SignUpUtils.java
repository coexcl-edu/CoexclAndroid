package com.home.coexcleducation.ui.registration;

import android.content.Context;

import com.home.coexcleducation.httphelper.HttpClient;
import com.home.coexcleducation.httphelper.HttpContentType;
import com.home.coexcleducation.httphelper.HttpHelper;
import com.home.coexcleducation.httphelper.HttpMethod;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.utils.ApiConstant;
import com.home.coexcleducation.utils.CoexclLogs;

public class SignUpUtils {

    public HttpHelper signup(Context context, HttpHelper lHttpHelper) {
        try {
            lHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            lHttpHelper.setRequestType(HttpMethod.POST.name());
            lHttpHelper.setUrl(ApiConstant.SIGN_UP_API);
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpHelper updateProfile(Context context, HttpHelper lHttpHelper) {
        try {
            lHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            lHttpHelper.setRequestType(HttpMethod.PUT.name());
            lHttpHelper.setUrl(ApiConstant.SIGN_UP_API);
            CoexclLogs.errorLog("SignUpUtils", "Req url profile - "+lHttpHelper.getUrl());
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public HttpHelper login(Context context, HttpHelper lHttpHelper) {
        try {
            lHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            lHttpHelper.setRequestType(HttpMethod.POST.name());
            lHttpHelper.setUrl(ApiConstant.LOGIN_API);
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpHelper sendOtp(Context context, HttpHelper lHttpHelper) {
        try {
            lHttpHelper.setRequestType(HttpMethod.GET.name());
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpHelper findSchool(Context context, String lSchoolCode) {
        try {
            HttpHelper lHttpHelper = new HttpHelper();
            lHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            lHttpHelper.setRequestType(HttpMethod.GET.name());
            lHttpHelper.setUrl(ApiConstant.SCHOOL_FETCH_API+"/"+lSchoolCode);
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpHelper forgotPasswrod(Context context, HttpHelper lHttpHelper) {
        try {
            lHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            lHttpHelper.setRequestType(HttpMethod.POST.name());
            lHttpHelper.setUrl(ApiConstant.FORGET_PASSWORD_API);
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpHelper changePassword(Context context, HttpHelper lHttpHelper) {
        try {
            lHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            lHttpHelper.setRequestType(HttpMethod.POST.name());
            lHttpHelper.setUrl(ApiConstant.CHANGE_PASSWORD_API);
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
