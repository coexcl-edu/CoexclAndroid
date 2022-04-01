package com.home.coexcleducation.httphelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.home.coexcleducation.MainApplication;
import com.home.coexcleducation.jdo.UserDetails;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.Helper;
import com.home.coexcleducation.utils.PreferenceHelper;
import com.home.coexcleducation.utils.ViewUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Vignesh on 13/11/15.
 */
public class HttpClient {

    final static String                     TAG = "HttpClient";
    Context                                 mContext;
    SharedPreferences                       mSharedPreferences;

    public HttpClient(Context mContext) {
        this.mContext                       = mContext;
        mSharedPreferences                  = PreferenceHelper.getSharedPreference(mContext);
    }

    public HttpHelper executeHttpRequest(HttpHelper pHttphelper) throws Exception {

        if(Helper.isOnline(mContext)) {

            String lResult = null;
            StringBuilder lResponse = new StringBuilder();
            URL lGetMessageUrl = new URL(pHttphelper.getUrl());
            HttpURLConnection urlConnection = (HttpURLConnection) lGetMessageUrl.openConnection();

            try {
                if (pHttphelper.getHeader() != null && pHttphelper.getHeader().size() > 0) {
                    for (String lKey : pHttphelper.getHeader().keySet())
                        urlConnection.addRequestProperty(lKey, pHttphelper.getHeader().get(lKey));
                }

                urlConnection.setRequestMethod(pHttphelper.getRequestType());

                if (pHttphelper.getRequestType().equals(HttpMethod.PATCH.name())) {
                    urlConnection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                    urlConnection.setRequestMethod("POST");
                }

//            urlConnection.setRequestProperty("User-agent", Helper.getSetmoreUserAgent(mContext));
//            urlConnection.setRequestProperty("X-SM-DUID", Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));

                if (pHttphelper.getRequestType().equals(HttpMethod.POST.name()) || pHttphelper.getRequestType().equals(HttpMethod.PUT.name())) {
                    urlConnection.setDoOutput(true);

                    if (HttpContentType.HTTP_CONTENTTYPE_JSON.equalsIgnoreCase(pHttphelper.getContentType())) {
                        urlConnection.setRequestProperty("Content-Type", HttpContentType.HTTP_CONTENTTYPE_JSON);
                    }

                    if (pHttphelper.getPayload() != null) {
                        OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                        wr.write(pHttphelper.getPayload().toString().trim());
                        wr.flush();
                    }

                } else if (pHttphelper.getRequestType().equals(HttpMethod.DELETE.name())) {
                    if (HttpContentType.HTTP_CONTENTTYPE_JSON.equalsIgnoreCase(pHttphelper.getContentType())) {
                        urlConnection.setRequestProperty("Content-Type", HttpContentType.HTTP_CONTENTTYPE_JSON);
                    }
                    if (pHttphelper.getPayload() != null) {
                        OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                        wr.write(pHttphelper.getPayload().trim());
                        wr.flush();
                    }
                }

                try {
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK || urlConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                        InputStream inputStream = urlConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        while ((lResult = bufferedReader.readLine()) != null) {
                            lResponse.append(lResult);
                        }
                        pHttphelper.setResponse(lResponse.toString());
                    } else if (urlConnection.getResponseCode() == 400) {
                        InputStream inputStream = urlConnection.getErrorStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        while ((lResult = bufferedReader.readLine()) != null) {
                            lResponse.append(lResult);
                        }
                        pHttphelper.setResponse(lResponse.toString());
                    } else if (urlConnection.getResponseCode() == 403) {  //todo will be removed when login enabled for setmore health accounts
                        InputStream inputStream = urlConnection.getErrorStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        while ((lResult = bufferedReader.readLine()) != null) {
                            lResponse.append(lResult);
                        }
                        pHttphelper.setResponse(lResponse.toString());
                    }
                    pHttphelper.setStatusCode(urlConnection.getResponseCode());
                    CoexclLogs.infoLog(TAG, "Http Response Code - " + urlConnection.getResponseCode());
                    CoexclLogs.infoLog(TAG, "Http Response - " + lResponse.toString());
                } catch (IOException e) {
                    Log.e(TAG, "Exception while executeHttpRequest - ", e);
                } catch (StackOverflowError error) {
                    Log.e(TAG, "StackOverflowError while executeHttpRequest - ", error);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                CoexclLogs.printException(e);
            }
        } else {
            Toast.makeText(mContext, "Internet is not available!", Toast.LENGTH_SHORT).show();
        }
        return pHttphelper;
    }


    public static HttpHelper fileUploader(File pSourceFile, HttpHelper pHttpHelper, Context pContext) {
        String charset = "UTF-8";
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URLConnection connection = null;
        try {
            connection = new URL(pHttpHelper.getUrl()).openConnection();
        } catch (IOException e) {
            CoexclLogs.printException(e);
        }
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        if (pHttpHelper.getHeader() != null && pHttpHelper.getHeader().size() > 0) {
            for (String lKey : pHttpHelper.getHeader().keySet())
                connection.addRequestProperty(lKey, pHttpHelper.getHeader().get(lKey));
        }

        try {
            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            String lContentType = URLConnection.guessContentTypeFromName(pSourceFile.getName());

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + pSourceFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + lContentType).append(CRLF);
            writer.append("userid: " + UserDetails.getInstance().getID());
            writer.append(CRLF).flush();

            InputStream is = null;
            try {
                is = new FileInputStream(pSourceFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            } finally {
                is.close();
            }


            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


            writer.append("--" + boundary + "--").append(CRLF).flush();
            pHttpHelper.setStatusCode(((HttpURLConnection) connection).getResponseCode());
            CoexclLogs.errorLog(TAG, "Response Code Image Uplaod " + ((HttpURLConnection) connection).getResponseCode());

            StringBuffer lBuffer = new StringBuffer();
            InputStream inputStream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String lResult = null;
            while ((lResult = bufferedReader.readLine()) != null) {
                lBuffer.append(lResult);
            }
            pHttpHelper.setResponse(lBuffer.toString());
        } catch (IOException e) {
            CoexclLogs.printException(e);
        }
        return pHttpHelper;
    }


    public static HttpHelper patchRequest(HttpHelper httpHelper) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .url(httpHelper.getUrl())
                .addHeader("Authorization", httpHelper.getHeader().get("Authorization"))
                .patch(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"), httpHelper.getPayload()));

        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            String lresponse = response.body().string();
            httpHelper.setResponse(lresponse);
        } catch (IOException e) {
            CoexclLogs.printException(e);
        }
        return httpHelper;
    }
}
