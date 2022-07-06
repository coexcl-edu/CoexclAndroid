package com.home.coexcleducation.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.coexcleducation.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {


    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public static float convertDpToPixel(float dp, Context pContext) {

        return dp * ((float) pContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public String constructJson(Object object) throws IOException {

        ObjectMapper lMapper = new ObjectMapper();
        Writer lWriter = new StringWriter();

        lMapper.writeValue(lWriter, object);
        return lWriter.toString();
    }

    public void playSound(Context activity ) {
        MediaPlayer mediaPlayer  = MediaPlayer.create(activity, R.raw.water);
        mediaPlayer.start();
    }


    public static void copyStream(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }

    public boolean isValidEmailID(String emailAddress) {
        if (emailAddress == null)
            return false;

        String  expression="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);
        if(matcher.find()){
            vId = matcher.group();
        } else {
            vId = "error";
        }
        CoexclLogs.errorLog("VideoPlayer", "vId - "+vId);
        return vId;
    }

    public String getCurrentMonth(){
        SimpleDateFormat lParse = new SimpleDateFormat("MMM");
        return lParse.format(new Date());
    }


}
