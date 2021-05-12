package com.home.coexcleducation.utils;

import android.util.Log;

public class CoexclLogs {

    public static void infoLog(String lActivityName , String lMessage) {
//        if(ApiConstants.SHOW_LOGS)
            Log.i(lActivityName, lMessage);
    }


    /**
     * Error Log
     * @param lActivityName
     * @param lMessage
     * @param lException
     */
    public static void errorLog(String lActivityName , String lMessage , Exception lException) {
//        if(ApiConstants.SHOW_LOGS)
            Log.e(lActivityName, lMessage, lException);
    }

    /**
     * Error Log
     * @param lActivityName
     * @param lMessage
     */
    public static void errorLog(String lActivityName , String lMessage) {
//        if(ApiConstants.SHOW_LOGS)
            Log.e(lActivityName, lMessage);
    }


    /**
     * Debug Log
     * @param lActivityName
     * @param lMessage
     */
    public static void debugLog(String lActivityName , String lMessage) {
//        if(ApiConstants.SHOW_LOGS)
            Log.d(lActivityName, lMessage);
    }


    /**
     * Warning Log
     * @param lActivityName
     * @param lMessage
     */
    public static void warningLog(String lActivityName , String lMessage) {
//        if(ApiConstants.SHOW_LOGS)
            Log.w(lActivityName, lMessage);
    }


    /**
     * Verbose Log
     * @param lActivityName
     * @param lMessage
     */
    public static void verboseLog(String lActivityName , String lMessage) {
//        if(ApiConstants.SHOW_LOGS)
            Log.v(lActivityName, lMessage);
    }

    public static void printException(Exception exception) {
//        if(ApiConstants.SHOW_LOGS)
            exception.printStackTrace();
    }

    public static void printException(Exception exception, boolean trackEvent) {
//        if(ApiConstants.SHOW_LOGS)
            exception.printStackTrace();

//        if(trackEvent) {
//            Sentry.captureException(exception);
//        }
    }
}
