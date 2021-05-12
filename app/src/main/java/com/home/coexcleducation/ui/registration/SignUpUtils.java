package com.home.coexcleducation.ui.registration;

import android.content.Context;

import com.home.coexcleducation.httphelper.HttpClient;
import com.home.coexcleducation.httphelper.HttpContentType;
import com.home.coexcleducation.httphelper.HttpHelper;
import com.home.coexcleducation.httphelper.HttpMethod;

public class SignUpUtils {

    public HttpHelper signup(Context context, HttpHelper lHttpHelper) {
        try {
            lHttpHelper.setContentType(HttpContentType.HTTP_CONTENTTYPE_JSON);
            lHttpHelper.setRequestType(HttpMethod.POST.name());
            return new HttpClient(context).executeHttpRequest(lHttpHelper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
