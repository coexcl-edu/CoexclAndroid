package com.home.coexcleducation.httphelper;


import java.util.HashMap;

/**
 * Created by ABHIshek on 18/02/16.
 */
public class HttpHelper {

    String url;
    String payload;
    String contentType;
    String requestType;
    String response;
    int statusCode = 0;
    HashMap<String, String> header;



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "HttpHelper{" +
                "url='" + url + '\'' +
                ", payload='" + payload + '\'' +
                ", contentType='" + contentType + '\'' +
                ", requestType='" + requestType + '\'' +
                ", response='" + response + '\'' +
                ", statusCode=" + statusCode +
                ", header=" + header +
                '}';
    }
}
