package com.home.coexcleducation.ui.registration;

import android.content.Context;

import com.home.coexcleducation.httphelper.HttpClient;
import com.home.coexcleducation.httphelper.HttpContentType;
import com.home.coexcleducation.httphelper.HttpHelper;
import com.home.coexcleducation.httphelper.HttpMethod;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.utils.ApiConstant;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.Constants;

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

    public HttpHelper updateToken(Context context, HttpHelper lHttpHelper) {
        try {
            lHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            lHttpHelper.setRequestType(HttpMethod.PUT.name());
            lHttpHelper.setUrl(ApiConstant.FCM_TOKEN_UP_API);
            CoexclLogs.errorLog("SignUpUtils", "Req url profile - "+lHttpHelper.getUrl());
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpHelper updateLimitedSession(Context context) {
        try {
            HttpHelper lHttpHelper = new HttpHelper();
            lHttpHelper.setRequestType(HttpMethod.GET.name());
            lHttpHelper.setUrl(ApiConstant.UPDATE_LIMITED_SESSION+"/"+UserDetails.getInstance().getID());
            CoexclLogs.errorLog("SignUpUtils", "Req updateLimitedSession - "+lHttpHelper.getUrl());
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

    public HttpHelper getProfile(Context context, String userId) {
        try {
            HttpHelper lHttpHelper = new HttpHelper();
            lHttpHelper.setUrl(ApiConstant.GET_PROFILE_API+userId);
            lHttpHelper.setRequestType(HttpMethod.GET.name());
            CoexclLogs.errorLog("TAG", "get Profile URL - "+lHttpHelper.getUrl());
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
